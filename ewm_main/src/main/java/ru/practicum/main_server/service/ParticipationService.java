package ru.practicum.main_server.service;

import ru.practicum.main_server.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    //Private: Получение информации о запросах на участие в событии текущего пользователя
    List<ParticipationRequestDto> getEventParticipationByInitiator(Long userId, Long eventId);

    //Private: Подтверждение чужой заявки на участие в событии текущего пользователя
    ParticipationRequestDto approveEventRequest(Long userId, Long eventId, Long reqId);

    //Private: Отклонение чужой заявки на участие в событии текущего пользователя
    ParticipationRequestDto rejectEventRequest(Long userId, Long eventId, Long reqId);

    //Private: Получение информации о заявках текущего пользователя на участие в чужих событиях
    List<ParticipationRequestDto> getRequestsByUser(Long userId);

    //Private: Добавление запроса от текущего пользователя на участие в событии
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    //Private: Отмена своего запроса на участие в событии
    ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId);
}