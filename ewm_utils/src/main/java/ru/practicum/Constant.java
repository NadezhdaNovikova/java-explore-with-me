package ru.practicum;

import java.time.format.DateTimeFormatter;

public class Constant {
    public static final String DATE_TIME_STRING = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_STRING);
}