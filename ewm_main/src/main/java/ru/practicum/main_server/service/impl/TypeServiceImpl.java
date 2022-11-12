package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.mapper.TypeMapper;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.dto.type.TypeDto;
import ru.practicum.main_server.entity.Type;
import ru.practicum.main_server.repository.TypeRepository;
import ru.practicum.main_server.service.TypeService;
import ru.practicum.main_server.utils.CheckEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeServiceImpl implements TypeService {

    private final TypeRepository typeRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public TypeDto update(TypeDto typeDto) {
        Type type = check.checkAndGetType(typeDto.getId());
        if (typeRepository.findTypeByName(typeDto.getName()).isPresent()) {
            return TypeMapper.toTypeDto(typeRepository.findTypeByName(typeDto.getName()).get());
        }
        type.setName(typeDto.getName());
        return TypeMapper.toTypeDto(typeRepository.save(type));
    }

    @Transactional
    @Override
    public TypeDto add(NewTypeDto newTypeDto) {
        if (typeRepository.findTypeByName(newTypeDto.getName()).isPresent()) {
            return TypeMapper.toTypeDto(typeRepository.findTypeByName(newTypeDto.getName()).get());
        }
        Type type = TypeMapper.toType(newTypeDto);
        return TypeMapper.toTypeDto(typeRepository.save(type));
    }

    @Transactional
    @Override
    public void delete(Long typeId) {
        Type type = check.checkAndGetType(typeId);
        typeRepository.delete(type);
    }

    @Transactional
    @Override
    public List<TypeDto> getAllPublic(PageRequest pageRequest) {
        return typeRepository.findAll(pageRequest)
                .stream()
                .map(TypeMapper::toTypeDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public TypeDto getById(Long typeId) {
        return TypeMapper.toTypeDto(check.checkAndGetType(typeId));
    }
}
