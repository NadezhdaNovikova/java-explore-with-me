package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.CategoryDto;
import ru.practicum.main_server.dto.NewCategoryDto;
import ru.practicum.main_server.entity.Category;

@UtilityClass
public class CategoryMapper {
    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return new Category(
                null,
                newCategoryDto.getName()
        );
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName());
    }
}
