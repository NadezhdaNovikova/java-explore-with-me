package ru.practicum.main_server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import ru.practicum.Constant;
import ru.practicum.dto.stats.EndpointHit;
import ru.practicum.dto.stats.ViewStats;
import ru.practicum.main_server.entity.Event;
import ru.practicum.stats_client.HitClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class EventStats {

    private final HitClient hitClient;
    private final CheckEntity check;

    @Value("${app.name}")
    String app;

    public int getViewsById(Long eventId) {
        Event event = check.checkAndGetEvent(eventId);
        String start = event.getCreatedOn().format(Constant.DATE_TIME_FORMATTER);
        String end = LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER);
        ResponseEntity<Object> responseEntity = hitClient.getStats(
                start,
                end,
                new String[]{"/events/" + eventId},
                false);

        List<ViewStats> statsList = (List<ViewStats>) responseEntity.getBody();
        log.info("responseEntity {}", statsList);
        if (statsList != null && !statsList.isEmpty()) {
            return statsList.stream().findFirst().get().getHits();
        }
        return 0;
    }

    public void sendHitStat(HttpServletRequest request) {
        log.info("Request URL {}", request.getRequestURI());
        EndpointHit endpointHit = new EndpointHit(
                0L,
                app,
                request.getRequestURI(),
                request.getRemoteAddr(),
                LocalDateTime.now());
        hitClient.saveHit(endpointHit);
    }
}
