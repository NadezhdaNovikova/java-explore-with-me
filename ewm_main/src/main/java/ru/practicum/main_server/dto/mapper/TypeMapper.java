package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.main_server.dto.type.NewTypeDto;
import ru.practicum.main_server.dto.type.TypeDto;
import ru.practicum.main_server.entity.Type;

@UtilityClass
public class TypeMapper {
    public static Type toType(NewTypeDto newTypeDto) {
        return Type.builder()
                .name(newTypeDto.getName())
                .build();
    }

    public static TypeDto toTypeDto(Type type) {
        return new TypeDto(
                type.getId(),
                type.getName());
    }
}
