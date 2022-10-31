package ru.practicum.main_server.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.Constant;
import ru.practicum.main_server.dto.ParticipationRequestDto;
import ru.practicum.main_server.entity.Participation;

@UtilityClass
public class ParticipationMapper {
    public static ParticipationRequestDto toParticipationRequestDto(Participation participation) {
        return new ParticipationRequestDto(
                participation.getId(),
                participation.getCreated().format(Constant.DATE_TIME_FORMATTER),
                participation.getEvent().getId(),
                participation.getRequester().getId(),
                participation.getStatus().toString()
        );
    }
}
