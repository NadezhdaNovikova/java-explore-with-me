package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.entity.HitModel;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<HitModel, Long> {

    List<HitModel> findAllByUriAndTimestampBetween(String uri, LocalDateTime start, LocalDateTime end);

    List<HitModel> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
