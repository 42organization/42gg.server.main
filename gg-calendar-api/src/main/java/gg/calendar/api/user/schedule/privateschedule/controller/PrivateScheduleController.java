package gg.calendar.api.user.schedule.privateschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.calendar.api.user.schedule.privateschedule.service.PrivateScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/private")
public class PrivateScheduleController {
	private final PrivateScheduleService privateScheduleService;

	@PostMapping
	public ResponseEntity<Void> privateScheduleCreate(@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody PrivateScheduleCreateReqDto privateScheduleCreateReqDto) {
		privateScheduleService.createPrivateSchedule(userDto, privateScheduleCreateReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
