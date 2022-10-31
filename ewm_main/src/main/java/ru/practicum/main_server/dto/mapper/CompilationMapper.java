package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.CompilationDto;
import ru.practicum.main_server.dto.EventShortDto;
import ru.practicum.main_server.dto.NewCompilationDto;
import ru.practicum.main_server.entity.Compilation;
import ru.practicum.main_server.entity.Event;

import java.util.List;
import java.util.Set;

@UtilityClass
public class CompilationMapper {

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(
                0L,
                events,
                newCompilationDto.getTitle(),
                newCompilationDto.getPinned()
        );
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                compilation.getId(),
                events,
                compilation.getTitle(),
                compilation.isPinned()
        );
    }
}
