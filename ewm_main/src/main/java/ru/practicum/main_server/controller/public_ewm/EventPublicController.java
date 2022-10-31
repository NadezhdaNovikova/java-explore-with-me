package ru.practicum.main_server.controller.public_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.EventFullDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.service.EventService;
import ru.practicum.main_server.utils.EventStats;
import ru.practicum.main_server.utils.PublicEventSearchParams;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events")
public class EventPublicController {

    private final EventService eventService;
    private final EventStats statClient;

    @GetMapping()
    List<EventShortDto> getAllPublic(@RequestParam(name = "text", required = false) String text,
                                     @RequestParam(name = "categories", required = false) List<Long> categories,
                                     @RequestParam(name = "paid", defaultValue = "false") Boolean paid,
                                     @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                     @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                     @RequestParam(name = "onlyAvailable", defaultValue = "false")
                                     Boolean onlyAvailable,
                                     @RequestParam(name = "sort", required = false) String sort,
                                     @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                     Integer from,
                                     @Positive @RequestParam(name = "size", defaultValue = "10")
                                     Integer size, HttpServletRequest request) {
        log.info("Public access: get events");
        statClient.sendHitStat(request);
        return eventService
                .getAllPublic(new PublicEventSearchParams(
                        text,
                        categories,
                        paid,
                        rangeStart,
                        rangeEnd,
                        onlyAvailable,
                        sort,
                        from,
                        size,
                        request));
    }

    @GetMapping("/{id}")
    EventFullDto getById(@Positive @PathVariable("id") long id, HttpServletRequest request) {
        log.info("Public access: get event id = {}", id);
        statClient.sendHitStat(request);
        return eventService.getById(id);
    }
}
