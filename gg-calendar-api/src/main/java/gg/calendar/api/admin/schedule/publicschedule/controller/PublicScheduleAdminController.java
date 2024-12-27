package gg.calendar.api.admin.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.auth.UserDto;
import gg.auth.argumentresolver.Login;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/calendar/public")
public class PublicScheduleAdminController {

	private final PublicScheduleAdminService publicScheduleAdminService;

	@PostMapping
	public ResponseEntity<Void> createPublicSchedule(
		@RequestBody @Valid PublicScheduleAdminCreateReqDto publicScheduleAdminCreateReqDto,
		@Login @Parameter(hidden = true) UserDto userDto) {
		String intraId = userDto.getIntraId();
		publicScheduleAdminService.createPublicSchedule(publicScheduleAdminCreateReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
