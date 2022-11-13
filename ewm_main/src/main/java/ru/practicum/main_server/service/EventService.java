package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_server.dto.event.AdminUpdateEventRequest;
import ru.practicum.main_server.dto.event.EventFullDto;
import ru.practicum.main_server.dto.event.EventShortDto;
import ru.practicum.main_server.dto.event.NewEventDto;
import ru.practicum.main_server.dto.event.UpdateEventRequest;
import ru.practicum.main_server.utils.AdminEventSearchParams;
import ru.practicum.main_server.utils.PublicEventSearchParams;

import java.util.List;

public interface EventService {
    //Admin: Поиск событий
    List<EventFullDto> getAdminEvents(AdminEventSearchParams params);

    //Admin: Редактирование события
    EventFullDto updateAdminEventById(Long eventId, AdminUpdateEventRequest request);

    //Admin: Публикация события
    EventFullDto publishAdminEventById(Long eventId);

    //Admin: Отклонение события
    EventFullDto rejectAdminEventById(Long eventId);

    //Private: Получение событий, добавленных текущим пользователем
    List<EventShortDto> getUserEvents(Long userId, PageRequest pageRequest);

    //Private: Изменение события добавленного текущим пользователем
    EventFullDto updateEvent(Long userId, UpdateEventRequest updateEventRequest);

    //Private: Отмена события добавленного текущим пользователем.
    EventFullDto cancelEvent(Long userId, Long eventId);

    //Public: Получение событий с возможностью фильтрации
    List<EventShortDto> getAllPublic(PublicEventSearchParams params);

    //Public: Получение подробной информации об опубликованном событии по его идентификатору
    EventFullDto getById(Long id);

    //Private: Добавление нового события
    EventFullDto add(long userId, NewEventDto newEventDto);

    //Private: Получение полной информации о событии добавленном текущим пользователем
    EventFullDto getUserEvent(Long userId, Long eventId);

    //Public: Получение списка событий в радиусе локации по ее id
    List<EventShortDto> getEventsByLocation(long locId, String sort, int from, int size);
}