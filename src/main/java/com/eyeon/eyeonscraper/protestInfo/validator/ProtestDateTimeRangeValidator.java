package com.eyeon.eyeonscraper.protestInfo.validator;

import com.eyeon.eyeonscraper.protestInfo.dto.ProtestCreateDto;
import jakarta.validation.ConstraintValidator;
import java.time.Duration;

import jakarta.validation.ConstraintValidatorContext;

public class ProtestDateTimeRangeValidator implements ConstraintValidator<ValidProtestDateTimeRange, ProtestCreateDto> {
    @Override
    public boolean isValid(ProtestCreateDto createDto, ConstraintValidatorContext context) {

        if (createDto == null || createDto.getStartDateTime() == null || createDto.getEndDateTime() == null) {
            return false; // Invalid if any of the fields are null
        }
        if (isInvalidTimePriority(createDto)) return false;
        return !isInValidTimeDiff(createDto);
    }

    private boolean isInValidTimeDiff(ProtestCreateDto createDto) {
        long hoursDifference = Duration.between(createDto.getStartDateTime(), createDto.getEndDateTime()).toHours();
        return hoursDifference < 1 || hoursDifference > 24;
    }

    private boolean isInvalidTimePriority(ProtestCreateDto createDto) {
        return createDto.getEndDateTime().isBefore(createDto.getStartDateTime());
    }
}
