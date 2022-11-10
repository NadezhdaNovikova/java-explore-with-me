package ru.practicum.stats_server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm_utils.Constant;
import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.ewm_utils.ViewStats;
import ru.practicum.stats_server.entity.HitModel;
import ru.practicum.stats_server.mapper.HitMapper;
import ru.practicum.stats_server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Transactional
    public EndpointHit save(EndpointHit hit) {
        HitModel hitModel = HitMapper.toHitModel(hit);
        return HitMapper.toEndpointHit(statsRepository.save(hitModel));
    }

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        List<ViewStats> viewStatsList = new ArrayList<>();
        LocalDateTime statsStart;
        LocalDateTime statsEnd;

        if (start.isEmpty()) {
            statsStart = LocalDateTime.MIN;
        } else {
            statsStart = LocalDateTime.parse(start, Constant.DATE_TIME_FORMATTER);
        }
        if (end.isEmpty()) {
            statsEnd = LocalDateTime.MAX;
        } else {
            statsEnd = LocalDateTime.parse(end, Constant.DATE_TIME_FORMATTER);
        }

        if (uris.isEmpty()) {
            uris = statsRepository.findAllByTimestampBetween(statsStart, statsEnd)
                    .stream()
                    .map(HitModel::getUri)
                    .distinct()
                    .collect(Collectors.toList());
        }
        for (String uri : uris) {
            List<HitModel> models = statsRepository.findAllByUriAndTimestampBetween(uri, statsStart, statsEnd);
            if (Boolean.TRUE.equals(unique)) {
                models = models
                        .stream()
                        .filter(distinctByKey(HitModel::getIp))
                        .collect(Collectors.toList());
            }
            if (!models.isEmpty()) {
                viewStatsList.add(new ViewStats(
                        models.get(0).getApp(),
                        uri,
                        models.size()));
            }
        }
        return viewStatsList;
    }

    public static <T> Predicate<T> distinctByKey(
            Function<? super T, ?> keyExtractor) {

        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}