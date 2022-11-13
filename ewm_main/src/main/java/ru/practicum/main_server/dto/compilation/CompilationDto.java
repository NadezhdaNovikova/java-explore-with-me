package ru.practicum.main_server.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_server.dto.event.EventShortDto;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {

    @NotBlank
    private Long id;

    private List<EventShortDto> events;

    @NotBlank
    private String title;

    @NotBlank
    private Boolean pinned;
}
