package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.location.LocationDto;
import ru.practicum.main_server.dto.location.LocationShortDto;
import ru.practicum.main_server.dto.location.NewLocationDto;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.Type;

@UtilityClass
public class LocationMapper {

    public static Location toLocation(NewLocationDto newDto, Type type) {
        return Location.builder()
                .lat(newDto.getLat())
                .lon(newDto.getLon())
                .name(newDto.getName())
                .type(type)
                .description(newDto.getDescription())
                .radius(newDto.getRadius())
                .distance(0F)
                .build();
    }

    public static LocationDto toLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .lat(location.getLat())
                .lon(location.getLon())
                .name(location.getName())
                .type(TypeMapper.toTypeDto(location.getType()))
                .description(location.getDescription())
                .radius(location.getRadius())
                .distance(location.getDistance())
                .build();
    }

    public LocationShortDto toLocationShortDto(Location location) {
        return LocationShortDto.builder()
                .id(location.getId())
                .lat(location.getLat())
                .lon(location.getLon())
                .name(location.getName())
                .type(TypeMapper.toTypeDto(location.getType()))
                .description(location.getDescription())
                .radius(location.getRadius())
                .build();
    }
}
