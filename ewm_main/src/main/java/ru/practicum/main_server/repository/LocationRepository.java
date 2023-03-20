package ru.practicum.main_server.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.entity.Location;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findLocationByLatAndLon(float lat, float lon);

    Page<Location> findAll(Pageable pageable);

    @Query("SELECT l FROM Location AS l " +
            "WHERE function('distance', l.lat, l.lon, :lat, :lon) <= :radius")
    List<Location> findLocationsInRadius(float lat, float lon, float radius);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("UPDATE Location l " +
            " SET l.distance = function('distance', l.lat, l.lon, :lat, :lon) " +
            " WHERE function('distance', l.lat, l.lon, :lat, :lon) <= :radius")
    void setDistanceForLocationInRadiusCurrentPoint(float lat, float lon, float radius);

    @Query("select l from Location l where l.id in ?1")
    List<Location> findAllByIdIn(List<Long> ids, PageRequest pageable);
}