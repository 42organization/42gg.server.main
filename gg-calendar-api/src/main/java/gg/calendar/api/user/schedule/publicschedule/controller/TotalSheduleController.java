package gg.calendar.api.user.schedule.publicschedule.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.user.schedule.publicschedule.controller.response.TotalScheduleRetrieveResDto;
import gg.calendar.api.user.schedule.publicschedule.service.PublicScheduleService;
import gg.calendar.api.user.schedule.publicschedule.service.TotalScheduleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class TotalSheduleController {
	private final PublicScheduleService publicScheduleService;
	private final TotalScheduleService totalScheduleService;

	@GetMapping("")
	public ResponseEntity<List<TotalScheduleRetrieveResDto>> totalScheduleRetrieve(
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate start,
		@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
		LocalDateTime startTime = start.atStartOfDay();
		LocalDateTime endTime = end.atTime(LocalTime.MAX);
		List<TotalScheduleRetrieveResDto> res = totalScheduleService.retrieveTotalSchedule(startTime, endTime);
		return ResponseEntity.ok(res);
	}
}
