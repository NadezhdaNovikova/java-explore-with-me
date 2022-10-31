package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.Constant;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewEventDto;
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
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                0,
                event.getCreatedOn().format(Constant.DATE_TIME_FORMATTER),
                event.getDescription(),
                event.getEventDate().format(Constant.DATE_TIME_FORMATTER),
                event.getId(),
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.isPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn() == null ? null : event.getPublishedOn()
                        .format(Constant.DATE_TIME_FORMATTER),
                event.isRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                0
        );
    }

    public static Event toEvent(NewEventDto newEventDto,
                                User initiator,
                                Category category,
                                Location location) {
        return new Event(
                0L,
                newEventDto.getAnnotation(),
                category,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                LocalDateTime.parse(newEventDto.getEventDate(), Constant.DATE_TIME_FORMATTER),
                initiator,
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                null,
                newEventDto.getRequestModeration(),
                State.PENDING,
                newEventDto.getTitle(),
                0
        );
    }
}
