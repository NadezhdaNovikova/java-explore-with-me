package ru.practicum.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.EndpointHit;
import ru.practicum.entity.HitModel;

@UtilityClass
public class HitMapper {

    public static HitModel toHitModel(EndpointHit hit) {
        return new HitModel(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
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
