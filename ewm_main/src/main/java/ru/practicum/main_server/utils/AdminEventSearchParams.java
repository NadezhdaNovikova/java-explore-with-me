package ru.practicum.main_server.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.Constant;
import ru.practicum.main_server.entity.enums.State;
import ru.practicum.main_server.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Getter
@Setter
@AllArgsConstructor
public class AdminEventSearchParams {
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Pageable page;

    public AdminEventSearchParams(List<Long> users,
                                  List<String> states,
                                  List<Long> categories,
                                  String rangeStart,
                                  String rangeEnd,
                                  Integer from,
                                  Integer size) throws ValidationException {
        this.users = users;
        this.states = states.stream().map(State::fromStringToState)
                .map(s -> s.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + s)))
                .collect(Collectors.toList());
        this.categories = categories;

        if (!isNull(rangeStart)) {
            this.rangeStart = LocalDateTime.parse(rangeStart, Constant.DATE_TIME_FORMATTER);
        }
        if (!isNull(rangeEnd)) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, Constant.DATE_TIME_FORMATTER);
        }

        if (!isNull(this.rangeStart)  && !isNull(this.rangeEnd) && this.rangeStart.isAfter(this.rangeEnd)) {
            throw new ValidationException(String.format("The start date %s cannot be later than the end date %s",
                    rangeStart, rangeEnd));
        }
        this.page = PageRequest.of(from / size, size);
    }
}
