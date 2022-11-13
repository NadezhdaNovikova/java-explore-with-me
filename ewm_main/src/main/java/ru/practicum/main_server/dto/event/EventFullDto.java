package ru.practicum.main_server.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.location.LocationShortDto;
import ru.practicum.main_server.dto.user.UserShortDto;

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
    private LocationShortDto location;

    private boolean requestModeration;
}
