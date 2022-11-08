package ru.practicum.main_server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_server.entity.Location;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventFullDto {

    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotBlank
    private CategoryDto category;

    @NotBlank
    private boolean paid;

    @NotBlank
    private String eventDate;

    @NotBlank
    private UserShortDto initiator;

    private int views;

    private int confirmedRequests;

    private String description;

    private int participantLimit;

    private String state;

    private String createdOn;

    private String publishedOn;

    @NotBlank
    private Location location;

    private boolean requestModeration;
}
