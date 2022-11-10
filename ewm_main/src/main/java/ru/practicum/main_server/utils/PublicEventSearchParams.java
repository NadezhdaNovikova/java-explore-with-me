package ru.practicum.main_server.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.ewm_utils.Constant;
import ru.practicum.main_server.entity.enums.SortParam;
import ru.practicum.main_server.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.Objects.isNull;

@Getter
@Setter
public class PublicEventSearchParams {
    private String text;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private SortParam sort;
    private Pageable page;
    private String addr;
    private String uri;

    public PublicEventSearchParams(String text, List<Long> categories, Boolean paid, String rangeStart, String rangeEnd,
                                  Boolean onlyAvailable, String sort, Integer from, Integer size,
                                  HttpServletRequest request)
            throws ValidationException {
        this.text = text;
        if (!isNull(categories)) {
            this.categories = categories;
        }
        this.paid = paid;
        if (!isNull(rangeStart)) {
            this.rangeStart = LocalDateTime.parse(rangeStart, Constant.DATE_TIME_FORMATTER);
        } else {
            this.rangeStart = LocalDateTime.now();
        }
        if (!isNull(rangeEnd)) {
            this.rangeEnd = LocalDateTime.parse(rangeEnd, Constant.DATE_TIME_FORMATTER);
        } else {
            this.rangeEnd = LocalDateTime.now().plusYears(10);
        }

        if (this.rangeStart.isAfter(this.rangeEnd)) {
            throw new ValidationException(String.format("The start date %s cannot be later than the end date %s",
                    rangeStart, rangeEnd));
        }
        this.onlyAvailable = onlyAvailable;
        this.sort = !isNull(sort) ? SortParam.fromStringToSort(sort)
                .orElseThrow(() -> new ValidationException("Unknown sort: " + sort)) : SortParam.VIEWS;
        this.page = this.sort == SortParam.EVENT_DATE
                ? PageRequest.of(from / size, size, Sort.Direction.ASC, "eventDate")
                : PageRequest.of(from / size, size);
        this.addr = request.getRemoteAddr();
        this.uri = request.getRequestURI();
    }
}
