package gg.calendar.api.user.custom.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.user.custom.controller.request.CalendarCustomCreateReqDto;
import gg.calendar.api.user.custom.controller.request.CalendarCustomUpdateReqDto;
import gg.calendar.api.user.custom.controller.response.CalendarCustomUpdateResDto;
import gg.calendar.api.user.custom.controller.response.CalendarCustomViewResDto;
import gg.calendar.api.user.custom.service.CalendarCustomService;
import gg.utils.dto.ListResponseDto;
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

	@PutMapping("/{id}")
	public ResponseEntity<CalendarCustomUpdateResDto> scheduleGroupUpdate(
		@Login @Parameter(hidden = true) UserDto userDto,
		@Valid @RequestBody CalendarCustomUpdateReqDto calendarCustomUpdateReqDto, @PathVariable Long id) {
		CalendarCustomUpdateResDto calendarCustomUpdateResDto = calendarCustomService.updateScheduleGroup(userDto,
			calendarCustomUpdateReqDto, id);
		return ResponseEntity.status(HttpStatus.OK).body(calendarCustomUpdateResDto);
	}

	@GetMapping
	public ResponseEntity<ListResponseDto<CalendarCustomViewResDto>> scheduleGroupViewGet(
		@Login @Parameter(hidden = true) UserDto userDto) {
		List<CalendarCustomViewResDto> response = calendarCustomService.getScheduleGroupView(userDto);
		return ResponseEntity.status(HttpStatus.OK).body(ListResponseDto.toDto(response));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> scheduleGroupDelete(@Login @Parameter(hidden = true) UserDto userDto,
		@PathVariable Long id) {
		calendarCustomService.deleteScheduleGroup(userDto, id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
