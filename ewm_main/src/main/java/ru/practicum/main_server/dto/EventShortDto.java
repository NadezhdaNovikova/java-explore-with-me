package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
