package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.dto.mapper.ParticipationMapper;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Participation;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.entity.enums.State;
import ru.practicum.main_server.entity.enums.StatusRequest;
import ru.practicum.main_server.exception.EntityNotFoundException;
import ru.practicum.main_server.exception.ValidationException;
import ru.practicum.main_server.repository.ParticipationRepository;
import ru.practicum.main_server.service.ParticipationService;
import ru.practicum.main_server.utils.CheckEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {

    private final ParticipationRepository participationRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public List<ParticipationRequestDto> getEventParticipationByInitiator(Long userId, Long eventId) {
        check.userExistById(userId);
        Event event = check.checkAndGetEvent(eventId);
        checkEventInitiator(event, userId);
        return participationRepository.findAllByEventId(eventId).stream()
                .map(ParticipationMapper::toParticipationRequestDto).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto approveEventRequest(Long userId, Long eventId, Long reqId) {
        Event event = check.checkAndGetEvent(eventId);
        checkEventInitiator(event, userId);
        Participation participation = check.checkAndGetParticipation(reqId);
        if (!participation.getStatus().equals(StatusRequest.PENDING)) {
            throw new ValidationException("Only status pending can be approval");
        }
        int countConfirmedRequests = participationRepository.countByEventIdAndStatus(eventId, StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() >= countConfirmedRequests) {
            participation.setStatus(StatusRequest.REJECTED);
        } else {
            participation.setStatus(StatusRequest.CONFIRMED);
        }
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    @Transactional
    @Override
    public ParticipationRequestDto rejectEventRequest(Long userId, Long eventId, Long reqId) {
        Event event = check.checkAndGetEvent(eventId);
        checkEventInitiator(event, userId);
        Participation participation = check.checkAndGetParticipation(reqId);

        participation.setStatus(StatusRequest.REJECTED);
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(participation));
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        User requester = check.checkAndGetUser(userId);
        return participationRepository.findAllByRequester(requester)
                .stream()
                .map(ParticipationMapper::toParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        User requester = check.checkAndGetUser(userId);
        Event event = check.checkAndGetEvent(eventId);
        if (participationRepository.findByEventAndRequester(event, requester) != null) {
            throw new ValidationException(String.format("Participation request from the user id = %d in " +
                    "the event id = %d already exists", userId, eventId));
        }
        if (event.getInitiator().equals(requester)) {
            throw new ValidationException("Initiator of an event cannot add a request to its event.");
        }
        if (!(event.getState().equals(State.PUBLISHED))) {
            throw new ValidationException("You сan not participate in an unpublished event!");
        }
        int confirmedRequests = participationRepository.countDistinctByEventAndStatus(event, StatusRequest.CONFIRMED);
        if (event.getParticipantLimit() != null && event.getParticipantLimit() != 0 && event
                .getParticipantLimit() <= confirmedRequests) {
            throw new ValidationException("Participant limit already full");
        }
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(
                new Participation(0L,
                        LocalDateTime.now(),
                        event,
                        requester,
                        event.isRequestModeration() ? StatusRequest.PENDING : StatusRequest.CONFIRMED)));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestByUser(Long userId, Long requestId) {
        check.userExistById(userId);
        Participation request = participationRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Participation request with id = %d not found.",
                        requestId)));
        request.setStatus(StatusRequest.CANCELED);
        return ParticipationMapper.toParticipationRequestDto(participationRepository.save(request));
    }

    private void checkEventInitiator(Event event, Long userId) {
        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new ValidationException(String.format("User id = %d is not the initiator of the event id = %d",
                    userId, event.getId()));
        }
    }
}
