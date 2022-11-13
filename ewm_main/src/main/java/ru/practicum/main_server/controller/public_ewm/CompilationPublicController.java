package ru.practicum.main_server.controller.public_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main_server.dto.compilation.CompilationDto;
import ru.practicum.main_server.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping()
    List<CompilationDto> getAllPublic(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0")
                                      Integer from,
                                      @Positive @RequestParam(name = "size", defaultValue = "10")
                                      Integer size) {
        log.info("Public access: get compilations pinned {}", pinned);
        return compilationService.getAllPublic(pinned, PageRequest.of(from / size, size));
    }

    @GetMapping("/{compId}")
    CompilationDto getById(@Positive @PathVariable("compId") long compId) {
        log.info("Public access: get compilation id = {}", compId);
        return compilationService.getById(compId);
    }
}
