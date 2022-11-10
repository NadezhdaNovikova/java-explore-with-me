package ru.practicum.main_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Participation;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.entity.enums.StatusRequest;

import java.util.List;
import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByRequester(User requester);

    Participation findByEventAndRequester(Event event, User requester);

    Integer countDistinctByEventAndStatus(Event event, StatusRequest status);

    Integer countByEventIdAndStatus(Long eventId, StatusRequest status);

    List<Participation> findAllByEventId(long eventId);

    Optional<Participation> findByIdAndRequesterId(long requestId, long userId);
}