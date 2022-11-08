package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.dto.mapper.CategoryMapper;
import ru.practicum.entity.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.service.CategoryService;
import ru.practicum.utils.CheckEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        return null;
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
