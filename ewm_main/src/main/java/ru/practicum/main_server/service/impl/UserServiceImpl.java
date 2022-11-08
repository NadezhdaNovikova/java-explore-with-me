package ru.practicum.main_server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.NewUserRequest;
import ru.practicum.main_server.dto.UserDto;
import ru.practicum.main_server.dto.mapper.UserMapper;
import ru.practicum.main_server.repository.UserRepository;
import ru.practicum.main_server.service.UserService;
import ru.practicum.main_server.utils.CheckEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final CheckEntity check;

    @Transactional
    @Override
    public List<UserDto> getUsers(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<UserDto> getUsersByIds(List<Long> ids, PageRequest pageRequest) {
        return userRepository.findAllByIdIn(ids, pageRequest)
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto add(NewUserRequest newUserRequest) {
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        log.info("Сервис. Удаляем пользователя {}", userId);
        check.userExistById(userId);
        userRepository.deleteById(userId);
    }
}
