package ru.practicum.main_server.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.user.UserShortDto;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private int confirmedRequests;

    @NotBlank
    private String eventDate;

    private Long id;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private Boolean paid;

    @NotBlank
    private String title;

    private int views;
}
