package gg.calendar.api.user.custom.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.custom.controller.request.CalendarCustomCreateReqDto;
import gg.calendar.api.user.custom.service.CalendarCustomService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/custom")
public class CalendarCustomController {
	private final CalendarCustomService calendarCustomService;

	@PostMapping
	public ResponseEntity<Void> scheduleGroupCreate(@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody CalendarCustomCreateReqDto calendarCustomCreateReqDto) {
		calendarCustomService.createScheduleGroup(userDto, calendarCustomCreateReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
