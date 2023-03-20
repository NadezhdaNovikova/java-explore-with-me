package ru.practicum.main_server.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_server.dto.event.EventFullDto;
import ru.practicum.main_server.dto.event.EventShortDto;
import ru.practicum.main_server.entity.enums.StatusRequest;
import ru.practicum.main_server.repository.ParticipationRepository;

@Component
@RequiredArgsConstructor
public class ViewsAndConfirmedReq {

    private final EventStats statsClient;
    private final ParticipationRepository participationRepository;

    public EventShortDto setConfirmedRequestsAndViewsEventShortDto(EventShortDto eventShortDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventShortDto.getId(), StatusRequest.CONFIRMED);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(statsClient.getViewsById(eventShortDto.getId()));
        return eventShortDto;
    }

    public EventFullDto setConfirmedRequestsAndViewsEventFullDto(EventFullDto eventFullDto) {
        int confirmedRequests = participationRepository
                .countByEventIdAndStatus(eventFullDto.getId(), StatusRequest.CONFIRMED);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(statsClient.getViewsById(eventFullDto.getId()));
        return eventFullDto;
    }
}
