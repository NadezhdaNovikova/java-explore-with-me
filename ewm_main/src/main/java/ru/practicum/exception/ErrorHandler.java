package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.Constant;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException e) {
        log.info("EntityNotFoundException. Объект не найден {}, статус {}", e.getStackTrace(),
                HttpStatus.CONFLICT);
        return new ApiError(null,
                e.getMessage(),
                "The required object was not found.",
                HttpStatus.NOT_FOUND.toString(),
                LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConstraintViolationException(ConstraintViolationException e) {
        log.info("ConstraintViolationException. Произошла ошибка {}, статус ошибки {}", e.getMessage(),
                HttpStatus.CONFLICT);
        return new ApiError(Collections.singletonList(Arrays.toString(e.getStackTrace())),
                e.getMessage(),
                "Integrity constraint has been violated",
                HttpStatus.CONFLICT.toString(),
                LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationException(final ValidationException e) {
        log.info("ValidationException. Произошла ошибка {}, статус ошибки {}", e.getMessage(),
                HttpStatus.BAD_REQUEST);
        return new ApiError(Collections.singletonList(Arrays.toString(e.getStackTrace())),
                e.getMessage(),
                "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST.toString(),
                LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER)
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.warn("Throwable. Произошла ошибка {}, статус ошибки {}" + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ApiError(Collections.singletonList(Arrays.toString(e.getStackTrace())),
                e.getMessage(),
                "Error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                LocalDateTime.now().format(Constant.DATE_TIME_FORMATTER)
        );
    }
}
