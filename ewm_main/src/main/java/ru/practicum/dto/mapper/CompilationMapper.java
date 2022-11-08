package ru.practicum.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.entity.Compilation;
import ru.practicum.entity.Event;

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
