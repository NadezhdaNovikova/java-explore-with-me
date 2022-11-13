package ru.practicum.main_server.controller.public_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.event.EventShortDto;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.utils.EventStats;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/locations")
public class LocationPublicController {

    private final EventService eventService;
    private final EventStats statsClient;

    @GetMapping("/{locId}")
    List<EventShortDto> getEventsByLocation(@Positive @PathVariable long locId,
                                            @RequestParam(name = "sort", required = false) String sort,
                                            @RequestParam(defaultValue = "0") int from,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        log.info("Public access: get events by location id = {}", locId);
        statsClient.sendHitStat(request);
        return eventService.getEventsByLocation(locId, sort, from, size);
    }
}
