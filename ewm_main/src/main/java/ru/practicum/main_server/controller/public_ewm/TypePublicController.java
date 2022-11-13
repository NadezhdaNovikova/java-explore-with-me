package ru.practicum.main_server.controller.public_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.type.TypeDto;
import ru.practicum.main_server.service.TypeService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/locations/types")
public class TypePublicController {

    private final TypeService typeService;

    @GetMapping
    List<TypeDto> getAllPublic(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                   Integer from,
                               @Positive @RequestParam(name = "size", defaultValue = "10")
                                   Integer size) {
        log.info("Public access: get location types");
        return typeService.getAllPublic(PageRequest.of(from / size, size));
    }

    @GetMapping("/{typeId}")
    TypeDto getById(@Positive @PathVariable("typeId") long typeId) {
        log.info("Public access: get location type by id = {}", typeId);
        return typeService.getById(typeId);
    }
}
