package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequest {

    @NotNull
    private Long eventId;
    private String annotation;
    private Long categoryId;
    private String description;
    private String eventDate;
    private Boolean paid;
    private Integer participantLimit;
    private String title;
}
