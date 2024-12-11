package gg.calendar.api.schedule.privateschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.calendar.api.schedule.privateschedule.service.PrivateScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/private")
public class PrivateScheduleController {
	private final PrivateScheduleService privateScheduleService;

	public ResponseEntity<Void> createPrivateSchedule(
		@Valid @ModelAttribute PrivateScheduleCreateReqDto privateScheduleCreateReqDto,
		@Login @Parameter(hidden = true) UserDto userDto) {
		privateScheduleService.createPrivateSchedule(userDto, privateScheduleCreateReqDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}


