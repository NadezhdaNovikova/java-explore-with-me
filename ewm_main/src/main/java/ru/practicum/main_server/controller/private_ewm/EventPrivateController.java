package ru.practicum.main_server.controller.private_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewEventDto;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.dto.UpdateEventRequest;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.service.ParticipationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/users/{userId}/events")
public class EventPrivateController {

    private final EventService eventService;
    private final ParticipationService participationService;

    @GetMapping()
    public List<EventShortDto> getUserEvents(@Positive @PathVariable("userId") Long userId,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                                 Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10")
                                                 Integer size) {
        log.info("Private access: Get events user id = {}", userId);
        return eventService.getUserEvents(userId, PageRequest.of(from / size, size));
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUserEvent(@Positive @PathVariable("userId") Long userId,
                                     @Positive @PathVariable("eventId") Long eventId) {
        log.info("Private access: Get event id = {} user id = {}", eventId, userId);
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping()
    public EventFullDto updateEvent(@Positive @PathVariable("userId") Long userId,
                                    @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Private access: Update event user id = {}", userId);
        return eventService.updateEvent(userId, updateEventRequest);
    }

    @PostMapping
    public EventFullDto add(@Positive @PathVariable("userId") Long userId,
                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Private access: Create event {} in category {} by user id = {}", newEventDto.getTitle(), newEventDto.getCategory(), userId);
        return eventService.add(userId, newEventDto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@Positive @PathVariable("userId") Long userId,
                                    @Positive @PathVariable("eventId") Long eventId) {
        log.info("Private access: Cancel event id = {}, user id = {}", eventId, userId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getEventParticipationByOwner(@Positive @PathVariable("userId") Long userId,
                                                                      @Positive @PathVariable("eventId") Long eventId) {
        log.info("Private access: Get event participations. Event id = {}, user id = {}", eventId, userId);
        return participationService.getEventParticipationByInitiator(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto approveEventRequest(@Positive @PathVariable("userId") Long userId,
                                                                    @Positive @PathVariable("eventId") Long eventId,
                                                                    @Positive @PathVariable("reqId") Long reqId) {
        log.info("Private access: approve participation request id = {}, user id = {}", reqId, userId);
        return participationService.approveEventRequest(userId, eventId, reqId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectEventRequest(@Positive @PathVariable("userId") Long userId,
                                                                   @Positive @PathVariable("eventId") Long eventId,
                                                                   @Positive @PathVariable("reqId") Long reqId) {
        log.info("Private access: reject participation request id = {}, user id = {}", reqId, userId);
        return participationService.rejectEventRequest(userId, eventId, reqId);
    }
}
