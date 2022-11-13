package ru.practicum.main_server.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.dto.type.TypeDto;

import java.util.List;

public interface TypeService {
    //Admin: Изменение типа локации
    TypeDto update(TypeDto typeDto);

    //Admin: Добавление нового типа локации
    TypeDto add(NewTypeDto newTypeDto);

    //Admin: Удаление типа локации
    void delete(Long typeId);

    //Public: Получение типов локаций
    List<TypeDto> getAllPublic(PageRequest pageRequest);

    //Public: Получение информации о типе локации по его идентификатору
    TypeDto getById(Long typeId);
}