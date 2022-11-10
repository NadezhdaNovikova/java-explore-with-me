package ru.practicum.main_server.controller.private_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.service.ParticipationService;

import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/requests")
public class RequestPrivateController {

    private final ParticipationService participationService;

    @GetMapping
    public List<ParticipationRequestDto> getRequestsByUser(@Positive @PathVariable("userId") Long userId) {
        log.info("Private access: Get participation requests by user id = {}", userId);
        return participationService.getRequestsByUser(userId);
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@Positive @PathVariable("userId") long userId,
                                                              @Positive @RequestParam("eventId") long eventId) {
        log.info("Private access: Create participation request by user id = {}, event id = {}", userId, eventId);
        return participationService.addRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@Positive @PathVariable("userId") Long userId,
                                                 @Positive @PathVariable("requestId") Long requestId) {
        log.info("Private access: Cancel participation request by user id = {}, request id = {}", userId, requestId);
        return participationService.cancelRequestByUser(userId, requestId);
    }
}
