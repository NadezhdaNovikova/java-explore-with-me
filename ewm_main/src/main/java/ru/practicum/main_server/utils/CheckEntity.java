package ru.practicum.main_server.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.entity.Compilation;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Participation;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.exception.EntityNotFoundException;
import ru.practicum.main_server.exception.ValidationException;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.repository.CompilationRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.ParticipationRepository;
import ru.practicum.main_server.repository.UserRepository;

@RequiredArgsConstructor
@Component
public class CheckEntity {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    public User checkAndGetUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id = '%d' not found", userId)));
    }

    public Category checkAndGetCategory(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Category with id = '%d' not found", catId)));
    }

    public Compilation checkAndGetCompilation(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Compilation with id = '%d' not found", compId)));
    }

    public Event checkAndGetEvent(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new EntityNotFoundException(String.format("Event with id = '%d' not found", eventId)));
    }

    public Participation checkAndGetParticipation(Long reqId) {
        return participationRepository.findById(reqId).orElseThrow(() ->
                new EntityNotFoundException(String.format("ParticipationRequest with id = '%d' not found", reqId)));
    }

    public void userExistById(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("User with id = '%d' not found", userId));
        }
    }

    public void checkEventsInitiator(Event event, long userId) {
        if (event.getInitiator().getId() != userId) {
            throw new ValidationException(String.format("User id = %d is not the initiator of the event id = %d",
                    userId, event.getId()));
        }
    }
}
