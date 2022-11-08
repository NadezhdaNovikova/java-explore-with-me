package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.entity.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByLatAndLon(float lat, float lon);
}