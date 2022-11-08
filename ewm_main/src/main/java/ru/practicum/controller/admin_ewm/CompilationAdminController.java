package ru.practicum.controller.admin_ewm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto add(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Admin access: Add new compilation {}", newCompilationDto.getTitle());
        return compilationService.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void delete(@Positive @PathVariable("compId") Long compId) {
        log.info("Admin access: Delete compilation id = {}", compId);
        compilationService.delete(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@Positive @PathVariable("compId") long compId,
                                           @Positive @PathVariable("eventId") long eventId) {
        log.info("Admin access: Delete event id = {} from compilation id = {}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public void addEventToCompilation(@Positive @PathVariable("compId") long compId,
                                      @Positive @PathVariable("eventId") long eventId) {
        log.info("Admin access: Add event id = {} to compilation id = {}", eventId, compId);
        compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void deleteCompilationFromMainPage(@Positive @PathVariable("compId") long compId) {
        log.info("Admin access: Delete  compilation id ={} from main page", compId);
        compilationService.deleteCompilationFromMainPage(compId);
    }

    @PatchMapping("/{compId}/pin")
    public void addCompilationToMainPage(@Positive @PathVariable("compId") long compId) {
        log.info("Admin access: Add compilation id = {} to main page", compId);
        compilationService.addCompilationToMainPage(compId);
    }
}
