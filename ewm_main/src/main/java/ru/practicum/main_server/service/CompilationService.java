package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_server.dto.compilation.CompilationDto;
import ru.practicum.main_server.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    //Admin: Добавление новой подборки
    CompilationDto add(NewCompilationDto newCompilationDto);

    //Admin: Удаление подборки
    void delete(Long compId);

    //Admin: Удалить событие из подборки
    void deleteEventFromCompilation(Long compId, Long eventId);

    //Admin: Добавить событие в подборку
    void addEventToCompilation(Long compId, Long eventId);

    //Admin: Открепить подборку на главной странице
    void deleteCompilationFromMainPage(Long compId);

    //Admin: Закрепить подборку на главной странице
    void addCompilationToMainPage(Long compId);

    //Public: Получение подборок событий
    List<CompilationDto> getAllPublic(Boolean pinned, PageRequest pageRequest);

    //Public: Получение подборки событий по его id
    CompilationDto getById(long id);
}