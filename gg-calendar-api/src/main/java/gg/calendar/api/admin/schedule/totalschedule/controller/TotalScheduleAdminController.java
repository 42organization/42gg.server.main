package gg.calendar.api.admin.schedule.totalschedule.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.admin.schedule.totalschedule.controller.response.TotalScheduleAdminResDto;
import gg.calendar.api.admin.schedule.totalschedule.service.TotalScheduleAdminService;
import gg.data.calendar.type.DetailClassification;
import gg.utils.dto.PageRequestDto;
import gg.utils.dto.PageResponseDto;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/calendar")
public class TotalScheduleAdminController {

	private final TotalScheduleAdminService totalScheduleAdminService;

	@GetMapping("/list/{detailClassification}")
	public ResponseEntity<PageResponseDto<TotalScheduleAdminResDto>> totalScheduleAdminClassificationList(
		@PathVariable DetailClassification detailClassification, @ModelAttribute @Valid PageRequestDto pageRequestDto) {
		int page = pageRequestDto.getPage();
		int size = pageRequestDto.getSize();

		PageResponseDto<TotalScheduleAdminResDto> pageResponseDto = totalScheduleAdminService.findAllByClassification(
			detailClassification, page, size);

		return ResponseEntity.ok(pageResponseDto);
	}

	@GetMapping
	public ResponseEntity<PageResponseDto<TotalScheduleAdminResDto>> totalScheduleAdminPageList(
		@ModelAttribute @Valid PageRequestDto pageRequestDto) {
		int page = pageRequestDto.getPage();
		int size = pageRequestDto.getSize();

		PageResponseDto<TotalScheduleAdminResDto> pageResponseDto = totalScheduleAdminService.findAll(page, size);

		return ResponseEntity.ok(pageResponseDto);
	}
}
