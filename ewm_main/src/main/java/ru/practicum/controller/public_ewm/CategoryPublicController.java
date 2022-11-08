package ru.practicum.controller.public_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CategoryDto;
import ru.practicum.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    List<CategoryDto> getAllPublic(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                   Integer from,
                                   @Positive @RequestParam(name = "size", defaultValue = "10")
                                   Integer size) {
        log.info("Public access: get categories");
        return categoryService.getAllPublic(PageRequest.of(from / size, size));
    }

    @GetMapping("/{catId}")
    CategoryDto getById(@Positive @PathVariable("catId") long catId) {
        log.info("Public access: get category by id = {}", catId);
        return categoryService.getById(catId);
    }
}
