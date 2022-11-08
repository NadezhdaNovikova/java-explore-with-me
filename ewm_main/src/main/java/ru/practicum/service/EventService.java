package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.AdminUpdateEventRequest;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewEventDto;
import ru.practicum.dto.UpdateEventRequest;
import ru.practicum.utils.AdminEventSearchParams;
import ru.practicum.utils.PublicEventSearchParams;

import javax.servlet.http.HttpServletRequest;
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

    void sendHitStat(HttpServletRequest request);
}