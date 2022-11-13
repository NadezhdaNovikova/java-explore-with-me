package ru.practicum.stats_server.service;

import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.ewm_utils.ViewStats;

import java.util.List;

public interface StatsService {

    //Сохранение информации об обращениях к эндпойнтам  GET /events/ и GET /events/{id} в публичном доступе
    EndpointHit save(EndpointHit hit);

    //Получение статистики за указанный период времени от start до end для списка эндпойнтов uris
    //с возможностью выборки по уникальным IP
    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}