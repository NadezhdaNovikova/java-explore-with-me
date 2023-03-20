package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.category.CategoryDto;
import ru.practicum.main_server.dto.category.NewCategoryDto;
import ru.practicum.main_server.dto.mapper.CategoryMapper;
import ru.practicum.main_server.entity.Category;
import ru.practicum.main_server.exception.EntityAlreadyExistException;
import ru.practicum.main_server.repository.CategoryRepository;
import ru.practicum.main_server.service.CategoryService;
import ru.practicum.main_server.utils.CheckEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        Category category = check.checkAndGetCategory(categoryDto.getId());
        category.setName(categoryDto.getName());
        try {
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistException(String.format("Category name %s is already exist",
                    category.getName()));
        }
    }

    @Transactional
    @Override
    public CategoryDto add(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        try {
            category = categoryRepository.save(category);
            log.info("CategoryServiceImpl: add — category was added {}.", category);
            return CategoryMapper.toCategoryDto(category);
        } catch (DataIntegrityViolationException e) {
            log.error("CategoryServiceImpl: add — category name {} is already exist",
                    category.getName());
            throw new EntityAlreadyExistException(String.format("Category name %s is already exist",
                    category.getName()));
        }
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
