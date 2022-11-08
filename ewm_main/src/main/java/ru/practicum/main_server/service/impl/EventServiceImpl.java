package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Constant;
import ru.practicum.dto.stats.EndpointHit;
import ru.practicum.dto.stats.ViewStats;
import ru.practicum.main_server.dto.AdminUpdateEventRequest;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewEventDto;
import ru.practicum.main_server.dto.UpdateEventRequest;
import ru.practicum.main_server.dto.mapper.EventMapper;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.entity.enums.SortParam;
import ru.practicum.main_server.entity.enums.State;
import ru.practicum.main_server.entity.enums.StatusRequest;
import ru.practicum.main_server.exception.ValidationException;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.LocationRepository;
import ru.practicum.main_server.repository.ParticipationRepository;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.utils.AdminEventSearchParams;
import ru.practicum.main_server.utils.CheckEntity;
import ru.practicum.main_server.utils.PublicEventSearchParams;
import ru.practicum.stats_client.HitClient;

import javax.servlet.http.HttpServletRequest;
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
    private final ParticipationRepository participationRepository;
    private final LocationRepository locationRepository;
    private final CheckEntity check;
    @Autowired
    private final HitClient hitClient;

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
                .map(this::setConfirmedRequestsAndViewsEventFullDto)
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
            float lat = eventUpdateDto.getLocation().getLat();
            float lon = eventUpdateDto.getLocation().getLon();
            Location location = locationRepository.findLocationByLatAndLon(lat, lon)
                    .orElse(locationRepository.save(new Location(0L, lat, lon)));
            event.setLocation(location);
        }

        Optional.ofNullable(eventUpdateDto.getRequestModeration())
                .ifPresent(event::setRequestModeration);

        event = eventRepository.save(event);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event);
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
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
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
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
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
    }

    @Transactional
    @Override
    public List<EventShortDto> getUserEvents(Long userId, PageRequest pageRequest) {
        return eventRepository.findAllByInitiatorId(userId, pageRequest)
                .stream()
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        check.userExistById(userId);
        Event event = check.checkAndGetEvent(eventId);
        check.checkEventsInitiator(event, userId);
        return setConfirmedRequestsAndViewsEventFullDto(EventMapper.toEventFullDto(event));
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
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);
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
        return setConfirmedRequestsAndViewsEventFullDto(eventFullDto);

    }

    @Transactional
    @Override
    public List<EventShortDto> getAllPublic(PublicEventSearchParams params) {
        List<Event> events = eventRepository.searchEvents(params.getText(), params.getCategories(), params.getPaid(),
                params.getRangeStart(), params.getRangeEnd(), params.getPage());

        SortParam sort = params.getSort();
        if (params.getSort().equals(SortParam.EVENT_DATE)) {
            events = events.stream()
                    .sorted(Comparator.comparing(Event::getEventDate))
                    .collect(Collectors.toList());
        }

        List<EventShortDto> eventShortDtos = events.stream()
                .filter(event -> event.getState().equals(State.PUBLISHED))
                .map(EventMapper::toEventShortDto)
                .map(this::setConfirmedRequestsAndViewsEventShortDto)
                .collect(Collectors.toList());
        if (sort.equals(SortParam.VIEWS)) {
            eventShortDtos = eventShortDtos.stream()
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        if (params.getOnlyAvailable()) {
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
        if (!(dto.getState().equals(State.PUBLISHED.toString()))) {
            throw new ValidationException(String.format("The event id = %d not published", eventId));
        }
        return setConfirmedRequestsAndViewsEventFullDto(dto);
    }

    @Transactional
    @Override
    public EventFullDto add(long userId, NewEventDto newEventDto) {
        User user = check.checkAndGetUser(userId);
        Category category = check.checkAndGetCategory(newEventDto.getCategoryId());
        float lat = newEventDto.getLocation().getLat();
        float lon = newEventDto.getLocation().getLon();
        Location location = locationRepository.findLocationByLatAndLon(lat, lon)
                .orElse(locationRepository.save(new Location(0L, lat, lon)));
        LocalDateTime eventDate;
        LocalDateTime checkStartDate = LocalDateTime.now().plusHours(2);
        ;
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
        Event event = eventRepository.save(EventMapper.toEvent(newEventDto, user, category, location));
        return EventMapper.toEventFullDto(event);
    }

    public EventShortDto setConfirmedRequestsAndViewsEventShortDto(EventShortDto eventShortDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventShortDto.getId(), StatusRequest.CONFIRMED);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(getViewsById(eventShortDto.getId()));
        return eventShortDto;
    }

    public EventFullDto setConfirmedRequestsAndViewsEventFullDto(EventFullDto eventFullDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventFullDto.getId(), StatusRequest.CONFIRMED);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(getViewsById(eventFullDto.getId()));
        return eventFullDto;
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

    public int getViewsById(Long eventId) {
        Event event = check.checkAndGetEvent(eventId);
        String start = event.getCreatedOn().format(Constant.DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER);
        ResponseEntity<Object> responseEntity = hitClient.getStats(
                start,
                end,
                new String[]{"/events/" + eventId},
                false);

        List<ViewStats> statsList = (List<ViewStats>) responseEntity.getBody();
        log.info("responseEntity {}", statsList);
        if (statsList != null && !statsList.isEmpty()) {
            return statsList.stream().findFirst().get().getHits();
        }
        return 0;
    }

    @Override
    public void sendHitStat(HttpServletRequest request) {
        log.info("Request URL {}", request.getRequestURI());
        EndpointHit endpointHit = new EndpointHit(
                0L,
                app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
        hitClient.saveHit(endpointHit);
    }
}