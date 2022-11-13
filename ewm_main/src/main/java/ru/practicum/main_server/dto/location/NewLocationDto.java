package ru.practicum.main_server.dto.location;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class NewLocationDto {
    @Digits(integer = 2, fraction = 6)
    @Range(min = -90, max = 90)
    @NotNull
    private float lat;

    @Digits(integer = 2, fraction = 6)
    @Range(min = -180, max = 180)
    @NotNull
    private float lon;

    private String name;

    private String description;

    private long type;

    private float radius;
}
