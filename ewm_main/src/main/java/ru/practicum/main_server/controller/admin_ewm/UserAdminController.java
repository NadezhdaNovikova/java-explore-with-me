package ru.practicum.main_server.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.user.NewUserRequest;
import ru.practicum.main_server.dto.user.UserDto;
import ru.practicum.main_server.exception.EntityNotFoundException;
import ru.practicum.main_server.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(
            @RequestParam(value = "ids", required = false) List<Long> ids,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        PageRequest pageable = PageRequest.of(from / size, size);
        if (ids == null) {
            log.info("Admin access: Get users");
            return userService.getUsers(pageable);
        } else {
            log.info("Admin access: Get users by id {}", ids);
            return userService.getUsersByIds(ids, pageable);
        }
    }

    @PostMapping
    public UserDto add(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Admin access: Add user {}", newUserRequest.getName());
        return userService.add(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void delete(@Positive @PathVariable("userId") Long userId) throws EntityNotFoundException {
        log.info("Admin access: Delete user id = {}", userId);
        userService.delete(userId);
    }
}