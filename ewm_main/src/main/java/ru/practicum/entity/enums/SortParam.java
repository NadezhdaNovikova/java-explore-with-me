package ru.practicum.entity.enums;

import java.util.Optional;

public enum SortParam {
    EVENT_DATE,
    VIEWS;

    public static Optional<SortParam> fromStringToSort(String stringSort) {
        for (SortParam sort : values()) {
            if (sort.name().equalsIgnoreCase(stringSort)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }
}
