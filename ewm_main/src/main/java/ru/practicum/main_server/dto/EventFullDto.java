package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_server.entity.Location;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    private int confirmedRequests;

    private String createdOn;

    private String description;

    @NotBlank
    private String eventDate;

    private Long id;

    @NotBlank
    private UserShortDto initiator;

    @NotBlank
    private Location location;

    @NotBlank
    private boolean paid;

    private int participantLimit;

    private String publishedOn;

    private boolean requestModeration;

    private String state;

    @NotBlank
    private String title;

    private int views;
}
