package ru.practicum.main_server.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.main_server.dto.mapper.TypeMapper;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.entity.Compilation;
import ru.practicum.main_server.entity.Event;
import ru.practicum.main_server.entity.Location;
import ru.practicum.main_server.entity.Participation;
import ru.practicum.main_server.entity.Type;
import ru.practicum.main_server.entity.User;
import ru.practicum.main_server.exception.EntityNotFoundException;
import ru.practicum.main_server.exception.ValidationException;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.repository.CompilationRepository;
import ru.practicum.main_server.repository.EventRepository;
import ru.practicum.main_server.repository.LocationRepository;
import ru.practicum.main_server.repository.ParticipationRepository;
import ru.practicum.main_server.repository.TypeRepository;
import ru.practicum.main_server.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class CheckEntity {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;
    private final LocationRepository locationRepository;
    private final TypeRepository typeRepository;

    private final static String DEFAULT_TYPE_NAME = "UNKNOWN STATE";

    public User checkAndGetUser(long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("User with id = '%d' not found", id)));
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

    public Location checkAndGetLocation(long id) {
        return locationRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Location with id = '%d' not found", id)));
    }

    public Type checkAndGetType(Long typeId) {
        return typeRepository.findById(typeId).orElseThrow(() ->
                new EntityNotFoundException(String.format("LocationType with id = '%d' not found", typeId)));
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

    public Type getLocationType(long typeIn) {
        Type type;
        if (typeIn == 0) {
            if (typeRepository.findTypeByName(DEFAULT_TYPE_NAME).isPresent()) {
                type = typeRepository.findTypeByName(DEFAULT_TYPE_NAME).get();
            } else {
                type = typeRepository.save(TypeMapper.toType(new NewTypeDto(DEFAULT_TYPE_NAME)));
            }
        } else {
            type = checkAndGetType(typeIn);
        }
        return type;
    }
}
