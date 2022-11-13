package ru.practicum.main_server.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.category.NewCategoryDto;
import ru.practicum.main_server.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryService categoryService;

    @GetMapping
    public String test() {
        return "Connect with Main Server";
    }

    @PatchMapping()
    public CategoryDto update(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Admin access: Update category id = {}", categoryDto.getId());
        log.info(categoryDto.getId() + categoryDto.getName());
        return categoryService.update(categoryDto);
    }

    @PostMapping()
    public CategoryDto add(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Admin access: Add category {}", newCategoryDto.getName());
        return categoryService.add(newCategoryDto);
    }

    @DeleteMapping("{catId}")
    public void delete(@Positive @PathVariable long catId) {
        log.info("Admin access: Delete category id = {}", catId);
        categoryService.delete(catId);
    }
}
