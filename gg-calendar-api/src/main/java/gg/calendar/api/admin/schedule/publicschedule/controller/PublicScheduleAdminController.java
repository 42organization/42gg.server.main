package gg.calendar.api.admin.schedule.publicschedule.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminUpdateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminResDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminUpdateResDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import gg.data.calendar.type.DetailClassification;
import gg.utils.dto.PageRequestDto;
import gg.utils.dto.PageResponseDto;
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

	@GetMapping("/list/{detailClassification}")
	public ResponseEntity<PageResponseDto<PublicScheduleAdminResDto>> publicScheduleAdminClassificationList(
		@PathVariable DetailClassification detailClassification, @ModelAttribute @Valid PageRequestDto pageRequestDto) {
		int page = pageRequestDto.getPage();
		int size = pageRequestDto.getSize();

		PageResponseDto<PublicScheduleAdminResDto> pageResponseDto = publicScheduleAdminService.findAllByClassification(
			detailClassification, page, size);

		return ResponseEntity.ok(pageResponseDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<PublicScheduleAdminUpdateResDto> publicScheduleUpdate(
		@RequestBody @Valid PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto,
		@PathVariable Long id) {
		PublicScheduleAdminUpdateResDto publicScheduleAdminUpdateRes = publicScheduleAdminService.updatePublicSchedule(
			publicScheduleAdminUpdateReqDto, id);
		return ResponseEntity.ok(publicScheduleAdminUpdateRes);
	}


	@PatchMapping("/{id}")
	public ResponseEntity<Void> publicScheduleDelete(@PathVariable Long id) {
		publicScheduleAdminService.deletePublicSchedule(id);
		return ResponseEntity.status(HttpStatus.OK).build();

	@GetMapping("/{id}")
	public ResponseEntity<PublicScheduleAdminResDto> publicScheduleDetail(@PathVariable Long id) {
		PublicScheduleAdminResDto publicScheduleAdminResDto = publicScheduleAdminService.detailPublicSchedule(id);
		return ResponseEntity.ok(publicScheduleAdminResDto);

	}
}
