package ru.practicum.main_server.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.ewm_utils.Constant;
import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.main_server.entity.Event;
import ru.practicum.stats_client.HitClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import static java.util.Objects.isNull;

@Slf4j
@Component
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


        if (!isNull(responseEntity.getBody()) && responseEntity.getBody().equals("")) {
            Integer hits = (Integer) ((LinkedHashMap) responseEntity.getBody()).get("hits");
            log.info("responseEntity {}", responseEntity.getBody());
            return hits;
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
