package ru.practicum.stats_server.service;

import ru.practicum.dto.stats.EndpointHit;
import ru.practicum.dto.stats.ViewStats;

import java.util.List;

public interface StatsService {
    EndpointHit save(EndpointHit hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}