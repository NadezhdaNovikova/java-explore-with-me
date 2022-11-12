package ru.practicum.stats_server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.ewm_utils.EndpointHit;
import ru.practicum.stats_server.entity.HitModel;

@UtilityClass
public class HitMapper {

    public static HitModel toHitModel(EndpointHit hit) {
        return HitModel.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public static EndpointHit toEndpointHit(HitModel hitModel) {
        return new EndpointHit(
                hitModel.getId(),
                hitModel.getApp(),
                hitModel.getUri(),
                hitModel.getIp(),
                hitModel.getTimestamp()
        );
    }
}
