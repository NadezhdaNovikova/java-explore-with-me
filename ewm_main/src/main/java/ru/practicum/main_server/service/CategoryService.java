package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    //Admin: Изменение категории
    CategoryDto update(CategoryDto categoryDto);

    //Admin: Добавление новой категории
    CategoryDto add(NewCategoryDto newCategoryDto);

    //Admin: Удаление категории
    void delete(Long catId);

    //Public: Получение категорий
    List<CategoryDto> getAllPublic(PageRequest pageRequest);

    //Public: Получение информации о категории по её идентификатору
    CategoryDto getById(Long catId);
}