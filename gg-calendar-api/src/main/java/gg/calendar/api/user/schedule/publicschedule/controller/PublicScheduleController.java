package gg.calendar.api.user.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.calendar.api.user.schedule.publicschedule.controller.response.PublicScheduleDetailRetrieveResDto;
import gg.calendar.api.user.schedule.publicschedule.controller.response.PublicScheduleUpdateResDto;
import gg.calendar.api.user.schedule.publicschedule.service.PublicScheduleService;
import gg.data.calendar.PublicSchedule;
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

	@PutMapping("/{id}")
	public ResponseEntity<PublicScheduleUpdateResDto> publicScheduleUpdate(@PathVariable Long id,
		@RequestBody @Valid PublicScheduleUpdateReqDto req,
		@Login @Parameter(hidden = true) UserDto userDto) {
		PublicSchedule updateSchedule = publicScheduleService.updatePublicSchedule(id, req, userDto.getId());
		return ResponseEntity.ok(PublicScheduleUpdateResDto.toDto(updateSchedule));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Void> publicScheduleDelete(@PathVariable Long id,
		@Login @Parameter(hidden = true) UserDto userDto) {
		publicScheduleService.deletePublicSchedule(id, userDto.getId());
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<PublicScheduleDetailRetrieveResDto> publicScheduleDetailRetrieveGet(@PathVariable Long id,
		@Login @Parameter(hidden = true) UserDto userDto) {
		PublicSchedule publicSchedule = publicScheduleService.getPublicScheduleDetailRetrieve(id, userDto.getId());
		return ResponseEntity.ok(PublicScheduleDetailRetrieveResDto.toDto(publicSchedule));
	}
}

