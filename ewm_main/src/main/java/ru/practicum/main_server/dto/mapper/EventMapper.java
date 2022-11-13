package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_utils.Constant;
import ru.practicum.main_server.dto.event.EventFullDto;
import ru.practicum.main_server.dto.event.EventShortDto;
import ru.practicum.main_server.dto.event.NewEventDto;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.entity.enums.State;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                0,
                event.getEventDate().format(Constant.DATE_TIME_FORMATTER),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.isPaid(),
                event.getTitle(),
                0
        );
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .paid(event.isPaid())
                .eventDate(event.getEventDate().format(Constant.DATE_TIME_FORMATTER))
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .views(event.getViews())
                .description(event.getDescription())
                .participantLimit(event.getParticipantLimit())
                .state(event.getState().toString())
                .createdOn(event.getCreatedOn().format(Constant.DATE_TIME_FORMATTER))
                .publishedOn(event.getPublishedOn() == null ? null : event.getPublishedOn()
                        .format(Constant.DATE_TIME_FORMATTER))
                .location(LocationMapper.toLocationShortDto(event.getLocation()))
                .requestModeration(event.isRequestModeration())
                .build();
    }

    public static Event toEvent(NewEventDto newEventDto,
                                User initiator,
                                Category category,
                                Location location) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), Constant.DATE_TIME_FORMATTER))
                .paid(newEventDto.getPaid())
                .location(location)
                .initiator(initiator)
                .participantLimit(newEventDto.getParticipantLimit())
                .state(State.PENDING)
                .title(newEventDto.getTitle())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }
}
