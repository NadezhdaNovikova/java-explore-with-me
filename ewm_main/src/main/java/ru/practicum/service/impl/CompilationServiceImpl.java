package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.mapper.CompilationMapper;
import ru.practicum.dto.mapper.EventMapper;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;
import ru.practicum.exception.ValidationException;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.service.CompilationService;
import ru.practicum.utils.CheckEntity;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;

    @Autowired
    private final CheckEntity check;

    @Override
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        Set<Event> events = newCompilationDto.getEvents()
                .stream()
                .map(check::checkAndGetEvent)
                .collect(Collectors.toSet());
        Compilation compilation = compilationRepository.save(new Compilation(0L,
                events,
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned()));
        return CompilationMapper.toCompilationDto(compilation, events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList()));
    }

    @Override
    public void delete(Long compId) {
        Compilation compilation = check.checkAndGetCompilation(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Compilation compilation = check.checkAndGetCompilation(compId);
        Set<Event> events = compilation.getEvents();
        events.remove(check.checkAndGetEvent(eventId));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Compilation compilation = check.checkAndGetCompilation(compId);
        Set<Event> events = compilation.getEvents();
        events.add(check.checkAndGetEvent(eventId));
        compilation.setEvents(events);
        compilationRepository.save(compilation);
    }

    @Override
    public void deleteCompilationFromMainPage(Long compId) {
        Compilation compilation = check.checkAndGetCompilation(compId);
        if (!compilation.isPinned()) {
            throw new ValidationException(String.format("Compilation id = '%s' already unpinned", compId));
        }
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    public void addCompilationToMainPage(Long compId) {
        Compilation compilation = check.checkAndGetCompilation(compId);
        if (compilation.isPinned()) {
            throw new ValidationException(String.format("Compilation id = '%s' already pinned", compId));
        }
        compilation.setPinned(true);
        compilationRepository.save(compilation);
    }

    @Override
    public List<CompilationDto> getAllPublic(Boolean pinned, PageRequest pageRequest) {
        Page<Compilation> compilations;
        if (isNull(pinned)) {
            compilations = compilationRepository.findAll(pageRequest);
        } else {
            compilations = compilationRepository.findAllByPinned(
                    pinned,
                    pageRequest
            );
        }
        return compilations
                .stream()
                .map(comp ->
                        CompilationMapper.toCompilationDto(comp, (comp.getEvents()
                                .stream()
                                .map(event -> check.checkAndGetEvent(event.getId()))
                                .collect(Collectors.toSet()))
                                .stream()
                                .map(EventMapper::toEventShortDto)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

    }

    @Override
    public CompilationDto getById(long id) {
        return null;
    }
}