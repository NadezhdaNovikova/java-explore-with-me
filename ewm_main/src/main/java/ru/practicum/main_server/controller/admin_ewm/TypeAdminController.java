package ru.practicum.main_server.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.dto.type.TypeDto;
import ru.practicum.main_server.service.TypeService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/locations/types")
public class TypeAdminController {

    private final TypeService typeService;

    @PatchMapping()
    public TypeDto update(@Valid @RequestBody TypeDto typeDto) {
        log.info("Admin access: Update location type id = {}", typeDto.getId());
        log.info(typeDto.getId() + typeDto.getName());
        return typeService.update(typeDto);
    }

    @PostMapping()
    public TypeDto add(@Valid @RequestBody NewTypeDto newTypeDto) {
        log.info("Admin access: Add location type {}", newTypeDto.getName());
        return typeService.add(newTypeDto);
    }

    @DeleteMapping("{typeId}")
    public void delete(@Positive @PathVariable("typeId") long typeId) {
        log.info("Admin access: Delete location type id = {}", typeId);
        typeService.delete(typeId);
    }
}
