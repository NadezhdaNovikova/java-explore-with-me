package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_utils.Constant;
import ru.practicum.main_server.dto.event.AdminUpdateEventRequest;
import ru.practicum.main_server.dto.event.EventFullDto;
import ru.practicum.main_server.dto.event.EventShortDto;
import ru.practicum.main_server.dto.event.NewEventDto;
import ru.practicum.main_server.dto.event.UpdateEventRequest;
import ru.practicum.main_server.dto.location.NewLocationDto;
import ru.practicum.main_server.dto.mapper.EventMapper;
import ru.practicum.main_server.dto.mapper.LocationMapper;
import ru.practicum.main_server.dto.mapper.TypeMapper;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.Type;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.entity.enums.SortParam;
import ru.practicum.main_server.entity.enums.State;
import ru.practicum.main_server.exception.ValidationException;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.LocationRepository;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.utils.AdminEventSearchParams;
import ru.practicum.main_server.utils.CheckEntity;
import ru.practicum.main_server.utils.PublicEventSearchParams;
import ru.practicum.main_server.utils.ViewsAndConfirmedReq;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CheckEntity check;
    private final ViewsAndConfirmedReq viewsConfirmed;

    @Value("${app.name}")
    String app;

    @Transactional
    @Override
    public List<EventFullDto> getAdminEvents(AdminEventSearchParams params) {
        List<Event> events = eventRepository.searchEventsByAdmin(params.getUsers(),
                params.getStates(),
                params.getCategories(),
                params.getRangeStart(),
                params.getRangeEnd(),
                params.getPage());

        return events.stream()
                .map(EventMapper::toEventFullDto)
                .map(viewsConfirmed::setConfirmedRequestsAndViewsEventFullDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateAdminEventById(Long eventId, AdminUpdateEventRequest eventUpdateDto) {
        Event event = check.checkAndGetEvent(eventId);
        setEventData(event,
                eventUpdateDto.getAnnotation(),
                eventUpdateDto.getCategoryId(),
                eventUpdateDto.getDescription(),
                eventUpdateDto.getParticipantLimit(),
                eventUpdateDto.getPaid(),
                eventUpdateDto.getTitle());

        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(LocalDateTime.parse(eventUpdateDto.getEventDate(),
                    Constant.DATE_TIME_FORMATTER));
        }
        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(checkEventLocation(eventUpdateDto.getLocation()));
        }

        Optional.ofNullable(eventUpdateDto.getRequestModeration())
                .ifPresent(event::setRequestModeration);

        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    @Override
    public EventFullDto publishAdminEventById(Long eventId) {
        Event event = check.checkAndGetEvent(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
            throw new ValidationException("The date of the event must be no earlier than 1 hour from the current date");
        }
        if (!event.getState().equals(State.PENDING)) {
            throw new ValidationException("You can only publish a pending event.");
        }
        event.setState(State.PUBLISHED);
        event.setPublishedOn(LocalDateTime.now());
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    @Override
    public EventFullDto rejectAdminEventById(Long eventId) {
        Event event = check.checkAndGetEvent(eventId);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Only unpublished events can be rejected");
        }
        event.setState(State.CANCELED);
        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest)
                .stream()
                .map(EventMapper::toEventShortDto)
                .map(viewsConfirmed::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        check.userExistById(userId);
        Event event = check.checkAndGetEvent(eventId);
        check.checkEventsInitiator(event, userId);
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(EventMapper.toEventFullDto(event));
    }

    @Transactional
    @Override
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Event event = check.checkAndGetEvent(updateEventRequest.getEventId());
        check.checkEventsInitiator(event, userId);
        checkState(event, "update");
        setEventData(event,
                updateEventRequest.getAnnotation(),
                updateEventRequest.getCategoryId(),
                updateEventRequest.getDescription(),
                updateEventRequest.getParticipantLimit(),
                updateEventRequest.getPaid(),
                updateEventRequest.getTitle());

        LocalDateTime eventDate;
        LocalDateTime checkStartDate = LocalDateTime.now().plusHours(2);
        if (!isNull(updateEventRequest.getEventDate())) {
            eventDate = LocalDateTime.parse(updateEventRequest.getEventDate(), Constant.DATE_TIME_FORMATTER);
            if (eventDate.isBefore(checkStartDate)) {
                throw new ValidationException(String.format(
                        "The date of the event cannot be earlier than in %s",
                        checkStartDate.format(Constant.DATE_TIME_FORMATTER)));
            }
            event.setEventDate(eventDate);
        }

        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        check.userExistById(userId);
        Event event = check.checkAndGetEvent(eventId);
        checkState(event, "cancel");
        check.checkEventsInitiator(event, userId);
        event.setState(State.CANCELED);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(eventRepository.save(event));
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(eventFullDto);

    }

    @Transactional
    @Override
    public List<EventShortDto> getAllPublic(PublicEventSearchParams params) {
        List<Event> events = eventRepository.searchEvents(params.getText(), params.getCategories(), params.getPaid(),
                params.getRangeStart(), params.getRangeEnd(), params.getPage());

        SortParam sort = params.getSort();
        if (sort.equals(SortParam.EVENT_DATE)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtos = getSortedList(events, sort);
        if (Boolean.TRUE.equals(params.getOnlyAvailable())) {
            eventShortDtos = eventShortDtos.stream()
                    .filter(eventShortDto -> eventShortDto.getConfirmedRequests()
                            <= check.checkAndGetEvent(eventShortDto.getId()).getParticipantLimit())
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }

    @Transactional
    @Override
    public EventFullDto getById(Long eventId) {
        EventFullDto dto = EventMapper.toEventFullDto(check.checkAndGetEvent(eventId));
        log.info(dto.getTitle() + " " + dto.getConfirmedRequests() + " " + dto.getViews());
        if (!(dto.getState().equals(State.PUBLISHED.toString()))) {
            throw new ValidationException(String.format("The event id = %d not published", eventId));
        }
        return viewsConfirmed.setConfirmedRequestsAndViewsEventFullDto(dto);
    }

    @Transactional
    @Override
    public EventFullDto add(long userId, NewEventDto newEventDto) {
        User user = check.checkAndGetUser(userId);
        Category category = check.checkAndGetCategory(newEventDto.getCategory());
        float lat = newEventDto.getLocation().getLat();
        float lon = newEventDto.getLocation().getLon();
        Location location = new Location();

        if (locationRepository.findLocationByLatAndLon(lat, lon).isPresent()) {
            location = locationRepository.findLocationByLatAndLon(lat, lon).get();
        } else {
            NewLocationDto newLocation = newEventDto.getLocation();

            Optional.ofNullable(newLocation.getName())
                    .ifPresent(location::setName);
            Optional.ofNullable(newLocation.getDescription())
                    .ifPresent(location::setDescription);
            Optional.of(newLocation.getRadius())
                    .ifPresent(location::setRadius);

            long typeIn = newLocation.getType();
            Type type = check.getLocationType(typeIn);
            location = LocationMapper.toLocation(newLocation, type);

        }

        Event event = EventMapper.toEvent(newEventDto, user, category, location);
        event.setLocation(checkEventLocation(event.getLocation()));

        LocalDateTime eventDate;
        LocalDateTime checkStartDate = LocalDateTime.now().plusHours(2);

        if (!isNull(newEventDto.getEventDate())) {
            eventDate = LocalDateTime.parse(newEventDto.getEventDate(), Constant.DATE_TIME_FORMATTER);
        } else {
            throw new ValidationException("Event date cannot be blank.");
        }

        if (eventDate.isBefore(checkStartDate)) {
            throw new ValidationException(String.format(
                    "The date of the event cannot be earlier than in %s",
                    checkStartDate.format(Constant.DATE_TIME_FORMATTER)));
        }
        return EventMapper.toEventFullDto(eventRepository.save(event));

    }

    @Transactional
    @Override
    public List<EventShortDto> getEventsByLocation(long locId, String sort, int from, int size) {
        Location location = check.checkAndGetLocation(locId);
        List<Event> events = eventRepository.searchEventsByLocation(location.getLat(),
                        location.getLon(),
                        location.getRadius(),
                        PageRequest.of(from / size, size))
                .stream()
                .collect(Collectors.toList());
        SortParam finalSort;
        finalSort = !isNull(sort) ? SortParam.fromStringToSort(sort)
                .orElseThrow(() -> new ValidationException("Unknown sort: " + sort)) : SortParam.EVENT_DATE;
        if (finalSort.equals(SortParam.EVENT_DATE)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }
        return getSortedList(events, finalSort);
    }


    private void checkState(Event event, String action) {
        if (action.equalsIgnoreCase("update") &&
                event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Published events can not be changed.");
        }
        if (action.equalsIgnoreCase("cancel") &&
                !event.getState().equals(State.PENDING)) {
            throw new ValidationException("You can cancel only pending event!");
        }
    }

    private void setEventData(Event event,
                              String annotation,
                              Long categoryId,
                              String description,
                              Integer participantLimit,
                              Boolean paid,
                              String title) {
        if (categoryId != null) {
            Category category = check.checkAndGetCategory(categoryId);
            event.setCategory(category);
        }

        Optional.ofNullable(annotation)
                .ifPresent(event::setAnnotation);

        Optional.ofNullable(description)
                .ifPresent(event::setDescription);

        if (participantLimit != null && participantLimit > 0) {
            event.setParticipantLimit(participantLimit);
        }

        Optional.ofNullable(paid)
                .ifPresent(event::setPaid);

        Optional.ofNullable(title)
                .ifPresent(event::setTitle);
    }

    private Location checkEventLocation(Location eventLocation) {
        Location location = new Location();
        if (eventLocation != null) {
            float lat = eventLocation.getLat();
            float lon = eventLocation.getLon();
            location = locationRepository.findLocationByLatAndLon(lat, lon)
                    .orElse(locationRepository.save(new Location(
                            0L,
                            lat,
                            lon,
                            isNull(eventLocation.getRadius()) ? 0 : eventLocation.getRadius(),
                            eventLocation.getName(),
                            eventLocation.getDescription(),
                            isNull(eventLocation.getType()) ?
                                    TypeMapper.toType(new NewTypeDto("UNKNOWN TYPE"))
                                    : eventLocation.getType(),
                            0F)));
        }
        return location;
    }

    private List<EventShortDto> getSortedList(List<Event> events, SortParam sort) {
        List<EventShortDto> eventShortDtos = events.stream()
                .filter(event -> event.getState().equals(State.PUBLISHED))
                .map(EventMapper::toEventShortDto)
                .map(viewsConfirmed::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals(SortParam.VIEWS)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        return eventShortDtos;
    }
}