package ru.practicum.main_server.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.AdminUpdateEventRequest;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.utils.AdminEventSearchParams;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/events")
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAdminEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                             @RequestParam(name = "states", required = false) List<String> states,
                                             @RequestParam(name = "categories", required = false) List<Long> categories,
                                             @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                             @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                             Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "10")
                                             Integer size) {
        log.info("Admin access: get events. Filter by users: {}, states: {}, category: {}", users, states, categories);

        return eventService.getAdminEvents(new AdminEventSearchParams(
                users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PutMapping("/{eventId}")
    public EventFullDto updateAdminEventById(@Positive @PathVariable("eventId") Long eventId,
                                             @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Admin access: update event id = {}", eventId);
        return eventService.updateAdminEventById(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishAdminEventById(@Positive @PathVariable("eventId") Long eventId) {
        log.info("Admin access: publish event id = {}", eventId);
        return eventService.publishAdminEventById(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectAdminEventById(@Positive @PathVariable("eventId") Long eventId) {
        log.info("Admin access: reject event id = {}", eventId);
        return eventService.rejectAdminEventById(eventId);
    }
}
