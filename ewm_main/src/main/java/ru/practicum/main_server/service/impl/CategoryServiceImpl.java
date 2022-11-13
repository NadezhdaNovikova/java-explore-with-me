package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.category.NewCategoryDto;
import ru.practicum.main_server.dto.mapper.CategoryMapper;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.service.CategoryService;
import ru.practicum.main_server.utils.CheckEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = check.checkAndGetCategory(categoryDto.getId());
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoryRepository.save(category));
    }

    @Transactional
    @Override
    public void delete(Long catId) {
        Category category = check.checkAndGetCategory(catId);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> getAllPublic(PageRequest pageRequest) {
        return categoryRepository.findAll(pageRequest)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        return CategoryMapper.toCategoryDto(check.checkAndGetCategory(catId));
    }
}
