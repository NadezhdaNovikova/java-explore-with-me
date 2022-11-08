package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.entity.Location;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminUpdateEventRequest {

    private String annotation;
    private Long categoryId;
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}
