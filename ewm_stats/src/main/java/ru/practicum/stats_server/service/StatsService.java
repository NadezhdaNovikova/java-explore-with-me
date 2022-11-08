package ru.practicum.stats_server.service;

import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.ewm_utils.ViewStats;

import java.util.List;

public interface StatsService {
    EndpointHit save(EndpointHit hit);

    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}