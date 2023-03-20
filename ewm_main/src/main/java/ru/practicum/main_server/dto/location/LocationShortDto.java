package ru.practicum.main_server.dto.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import ru.practicum.main_server.dto.type.TypeDto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class LocationShortDto {

    private long id;

    @Digits(integer = 2, fraction = 6)
    @Range(min = -90, max = 90)
    private float lat;

    @Digits(integer = 2, fraction = 6)
    @Range(min = -180, max = 180)
    private float lon;

    private String name;

    private String description;

    @NotBlank
    private TypeDto type;

    private float radius;
}
