package gg.calendar.api.admin.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/calendar/public")
public class PublicScheduleAdminController {

	private final PublicScheduleAdminService publicScheduleAdminService;

	@PostMapping
	public ResponseEntity<Void> publicScheduleCreate(
		@RequestBody @Valid PublicScheduleAdminCreateReqDto publicScheduleAdminCreateReqDto) {
		publicScheduleAdminService.createPublicSchedule(publicScheduleAdminCreateReqDto);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
