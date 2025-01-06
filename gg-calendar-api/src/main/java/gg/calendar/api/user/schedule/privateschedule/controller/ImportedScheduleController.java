package gg.calendar.api.user.schedule.privateschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.schedule.privateschedule.controller.request.ImportedScheduleUpdateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.response.ImportedScheduleUpdateResDto;
import gg.calendar.api.user.schedule.privateschedule.service.ImportedScheduleService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/private/imported")
public class ImportedScheduleController {
	private final ImportedScheduleService importedScheduleService;

	@PutMapping("/{id}")
	public ResponseEntity<ImportedScheduleUpdateResDto> importedScheduleUpdate(
		@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody ImportedScheduleUpdateReqDto importedScheduleUpdateReqDto,
		@PathVariable Long id) {
		ImportedScheduleUpdateResDto importedScheduleUpdateResDto = importedScheduleService.updateImportedSchedule(
			userDto, importedScheduleUpdateReqDto, id);
		return ResponseEntity.status(HttpStatus.OK).body(importedScheduleUpdateResDto);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> importedScheduleDelete(@Login @Parameter(hidden = true) UserDto userDto,
		@PathVariable Long id) {
		importedScheduleService.deleteImportedSchedule(userDto, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
