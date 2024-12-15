package gg.calendar.api.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.calendar.api.schedule.publicschedule.service.PublicScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/public")
public class PublicScheduleController {
	private final PublicScheduleService publicScheduleService;

	@PostMapping("/create")
	public ResponseEntity<Void> createPublicSchedule(
		@Valid @ModelAttribute PublicScheduleCreateReqDto publicScheduleCreateReqDto,
		@Login @Parameter(hidden = true) UserDto userDto) {
		publicScheduleService.createPublicSchedule(userDto, publicScheduleCreateReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PatchMapping("/{scheduleId}/update")
	public ResponseEntity<Void> updatePublicSchedule(@PathVariable @Valid Long scheduleId,
		@Valid @ModelAttribute PublicScheduleUpdateReqDto publicScheduleUpdateReqDto,
		@Login @Parameter(hidden = true) UserDto userDto) {
		Long userId = userDto.getId();
		publicScheduleService.updatePublicSchedule(scheduleId, userId, publicScheduleUpdateReqDto);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PatchMapping("/{scheduleId}/delete")
	public ResponseEntity<Void> deletePublicSchedule(@PathVariable @Valid Long scheduleId,
		@Login @Parameter(hidden = true) UserDto uSerDto) {
		Long userId = uSerDto.getId();
		publicScheduleService.deletePublicSchedule(scheduleId, userId);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
