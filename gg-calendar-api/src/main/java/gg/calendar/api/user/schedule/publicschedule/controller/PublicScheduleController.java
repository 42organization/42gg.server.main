package gg.calendar.api.user.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.user.schedule.publicschedule.service.PublicScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/public")
public class PublicScheduleController {
	private final PublicScheduleService publicScheduleService;

	@PostMapping
	public ResponseEntity<Void> publicScheduleCreate(@RequestBody @Valid PublicScheduleCreateReqDto req,
		@Login @Parameter(hidden = true) UserDto userDto) {
		publicScheduleService.createPublicSchedule(req, userDto.getId());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}

