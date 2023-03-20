package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_server.dto.location.LocationDto;
import ru.practicum.main_server.dto.location.LocationShortDto;
import ru.practicum.main_server.dto.location.NewLocationDto;
import ru.practicum.main_server.dto.location.UpdateLocationDto;

import java.util.List;

public interface LocationService {
    //Admin: Получение информации о локациях
    List<LocationShortDto> getLocations(PageRequest pageRequest);

    //Admin: Получение информации о локациях по их id
    List<LocationShortDto> getLocationsByIds(List<Long> ids, PageRequest pageable);

    //Admin: Добавление новой локации
    LocationDto add(NewLocationDto newLocation);

    //Admin: Изменение данных локации
    LocationShortDto update(UpdateLocationDto locationDto);

    //Admin: Получение списка локаций в радиусе указанной локации с расстоянием до них
    List<LocationDto> distance(LocationDto locationDto);
}