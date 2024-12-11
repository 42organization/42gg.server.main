package gg.calendar.api.schedule.privateschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;

import gg.calendar.api.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.calendar.api.schedule.privateschedule.service.PrivateScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import gg.calendar.api.schedule.privateschedule.controller.request.PrivateScheduleUpdateReqDto;
import gg.calendar.api.schedule.privateschedule.controller.response.PrivateScheduleUpdateResDto;
import gg.data.calendar.PrivateSchedule;

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

	private final PrivateScheduleService privateScheduleService;

	@PutMapping("/{scheduleId}/update")
	public ResponseEntity<PrivateScheduleUpdateResDto> updatePrivateSchedule(@PathVariable @Valid Long scheduleId,
		@ModelAttribute @Valid PrivateScheduleUpdateReqDto privateScheduleUpdateReqDto,
		@Login @Parameter(hidden = true) UserDto userDto) {
		Long userId = userDto.getId();
		PrivateSchedule privateSchedule = privateScheduleService.updatePrivateSchedule(scheduleId, userId,
			privateScheduleUpdateReqDto);
		PrivateScheduleUpdateResDto responseDto = PrivateScheduleUpdateResDto.of(privateSchedule);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}
}


