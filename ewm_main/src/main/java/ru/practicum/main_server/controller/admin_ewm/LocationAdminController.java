package ru.practicum.main_server.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.location.LocationDto;
import ru.practicum.main_server.dto.location.LocationShortDto;
import ru.practicum.main_server.dto.location.NewLocationDto;
import ru.practicum.main_server.dto.location.UpdateLocationDto;
import ru.practicum.main_server.service.LocationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/locations")
public class LocationAdminController {

    private final LocationService locationService;

    @GetMapping
    public List<LocationShortDto> getLocations(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "50") Integer size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            log.info("Admin access: Get locations");
            return locationService.getLocations(pageable);
        } else {
            log.info("Admin access: Get locations by id {}", ids);
            return locationService.getLocationsByIds(ids, pageable);
        }
    }

    @PostMapping
    public LocationDto add(@Valid @RequestBody NewLocationDto newLocationDto) {
        log.info("Admin access: Add location {}", newLocationDto.getName());
        return locationService.add(newLocationDto);
    }

    @PatchMapping("/{locationId}")
    public LocationShortDto update(@Positive @PathVariable("locationId") Long locationId,
                              @Valid @RequestBody UpdateLocationDto locationDto) {
        log.info("Admin access: Update location id = {}", locationId);
        return locationService.update(locationDto);
    }

    @GetMapping("/distance")
    public List<LocationDto> distance(@RequestBody LocationDto locationDto) {
        log.info("distance location");
        return locationService.distance(locationDto);
    }
}
