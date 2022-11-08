package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Digits;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class LocationDto {
    @Digits(integer = 2, fraction = 6)
    @Range(min = -90, max = 90)
    private float lat;

    @Digits(integer = 2, fraction = 6)
    @Range(min = -180, max = 180)
    private float lon;
}
