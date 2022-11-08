package ru.practicum.dto.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.Constant;
import ru.practicum.dto.ParticipationRequestDto;
import ru.practicum.entity.Participation;

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
