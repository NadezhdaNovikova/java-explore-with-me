package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.location.LocationDto;
import ru.practicum.main_server.dto.location.LocationShortDto;
import ru.practicum.main_server.dto.location.NewLocationDto;
import ru.practicum.main_server.dto.location.UpdateLocationDto;
import ru.practicum.main_server.dto.mapper.LocationMapper;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.Type;
import ru.practicum.main_server.exception.EntityNotFoundException;
import ru.practicum.main_server.repository.LocationRepository;
import ru.practicum.main_server.service.LocationService;
import ru.practicum.main_server.utils.CheckEntity;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public LocationShortDto update(UpdateLocationDto locationDto) {
        Location location = locationRepository.findById(locationDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        location.setType(check.checkAndGetType(locationDto.getType()));
        Optional.ofNullable(locationDto.getLat())
                .ifPresent(location::setLat);
        Optional.ofNullable(locationDto.getLon())
                .ifPresent(location::setLon);
        Optional.ofNullable(locationDto.getName())
                .ifPresent(location::setName);
        Optional.ofNullable(locationDto.getRadius())
                .ifPresent(location::setRadius);
        return LocationMapper.toLocationShortDto(locationRepository.save(location));
    }

    @Transactional
    @Override
    public List<LocationShortDto> getLocations(PageRequest pageRequest) {
        return locationRepository.findAll(pageRequest)
                .stream()
                .map(LocationMapper::toLocationShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<LocationShortDto> getLocationsByIds(List<Long> ids, PageRequest pageable) {
        return locationRepository.findAllByIdIn(ids, pageable)
                .stream()
                .map(LocationMapper::toLocationShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public LocationDto add(NewLocationDto newLocation) {
        float lat = newLocation.getLat();
        float lon = newLocation.getLon();

        if (locationRepository.findLocationByLatAndLon(lat, lon).isPresent()) {
            return LocationMapper.toLocationDto(locationRepository.findLocationByLatAndLon(lat, lon).get());
        }

        long typeIn = newLocation.getType();
        Type type = check.getLocationType(typeIn);
        Location location = LocationMapper.toLocation(newLocation, type);

        return LocationMapper.toLocationDto(locationRepository.save(location));
    }

    @Transactional
    @Override
    public List<LocationDto> distance(LocationDto locationDto) {
        float lat = locationDto.getLat();
        float lon = locationDto.getLon();
        float radius = locationDto.getRadius();

        locationRepository.setDistanceForLocationInRadiusCurrentPoint(lat, lon, radius);
        return locationRepository.findLocationsInRadius(lat, lon, radius)
                .stream()
                .map(LocationMapper::toLocationDto)
                .collect(Collectors.toList());
    }
}
