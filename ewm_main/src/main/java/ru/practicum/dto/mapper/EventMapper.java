package ru.practicum.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.Constant;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.entity.Category;
import ru.practicum.entity.Event;
import ru.practicum.entity.Location;
import ru.practicum.entity.User;
import ru.practicum.entity.enums.State;

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
