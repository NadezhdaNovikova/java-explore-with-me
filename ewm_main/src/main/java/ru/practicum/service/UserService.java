package ru.practicum.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.dto.NewUserRequest;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface UserService {
    //Admin: Получение информации о пользователях
    List<UserDto> getUsers(PageRequest pageRequest);

    //Admin: Получение информации о пользователях по их id
    List<UserDto> getUsersByIds(List<Long> ids, PageRequest pageable);

    //Admin: Добавление нового пользователя
    UserDto add(NewUserRequest newUserRequest);

    //Admin: Удаление пользователя
    void delete(Long userId);
}