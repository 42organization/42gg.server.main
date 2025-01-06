package gg.calendar.api.user.schedule.publicschedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.response.TotalScheduleRetrieveResDto;
import gg.data.calendar.PublicSchedule;
import gg.repo.calendar.PublicScheduleRepository;
import gg.utils.dto.ListResponseDto;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.InvalidParameterException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TotalScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;

	public ListResponseDto<TotalScheduleRetrieveResDto> retrieveTotalSchedule(LocalDateTime start, LocalDateTime end) {
		validateTimeRange(start, end);
		List<PublicSchedule> schedules = publicScheduleRepository.findByStartTimeBetween(start, end);
		return ListResponseDto.toDto(
			schedules.stream().map(TotalScheduleRetrieveResDto::toDto).collect(Collectors.toList()));
	}

	private void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
		if (endTime.isBefore(startTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}
}
