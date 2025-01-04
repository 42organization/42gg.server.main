package gg.calendar.api.user.schedule.privateschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.schedule.privateschedule.controller.request.ImportedScheduleUpdateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleUpdateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.response.ImportedScheduleUpdateResDto;
import gg.calendar.api.user.schedule.privateschedule.controller.response.PrivateScheduleUpdateResDto;
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

	@PutMapping("/{id}")
	public ResponseEntity<PrivateScheduleUpdateResDto> privateScheduleUpdate(
		@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody PrivateScheduleUpdateReqDto privateScheduleUpdateReqDto,
		@PathVariable Long id) {
		PrivateScheduleUpdateResDto privateScheduleUpdateResDto = privateScheduleService.updatePrivateSchedule(userDto,
			privateScheduleUpdateReqDto, id);
		return ResponseEntity.status(HttpStatus.OK).body(privateScheduleUpdateResDto);
	}

	@PutMapping("/imported/{id}")
	public ResponseEntity<ImportedScheduleUpdateResDto> importedScheduleUpdate(
		@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody ImportedScheduleUpdateReqDto importedScheduleUpdateReqDto,
		@PathVariable Long id) {
		ImportedScheduleUpdateResDto importedScheduleUpdateResDto = privateScheduleService.updateImportedSchedule(
			userDto, importedScheduleUpdateReqDto, id);
		return ResponseEntity.status(HttpStatus.OK).body(importedScheduleUpdateResDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> privateScheduleDelete(@Login @Parameter(hidden = true) UserDto userDto,
		@PathVariable Long id) {
		privateScheduleService.deletePrivateSchedule(userDto, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
