package ru.practicum.stats_server.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.ewm_utils.ViewStats;
import ru.practicum.stats_server.service.StatsService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@Valid @NotNull @RequestParam String start,
                                    @Valid @NotNull @RequestParam String end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(name = "unique", defaultValue = "false")Boolean unique) {
        log.info("STAT SERVER: Get statistics for {} from {} to {}, unique {}", uris, start, end, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    void save(@Valid @RequestBody EndpointHit hit) {
        log.info("STAT SERVER: Save hit {} to stat server", hit);
        statsService.save(hit);
    }
}