package gg.calendar.api.admin.schedule.publicschedule.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.utils.dto.PageResponseDto;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleAdminService {

	private final PublicScheduleAdminRepository publicScheduleAdminRepository;

	@Transactional
	public void createPublicSchedule(PublicScheduleAdminCreateReqDto publicScheduleAdminCreateReqDto) {

		dateTimeErrorCheck(publicScheduleAdminCreateReqDto.getStartTime(),
			publicScheduleAdminCreateReqDto.getEndTime());

		PublicSchedule publicSchedule = PublicScheduleAdminCreateReqDto.toEntity(publicScheduleAdminCreateReqDto);
		publicScheduleAdminRepository.save(publicSchedule);
	}

	public PageResponseDto<PublicScheduleAdminResDto> findAllByClassification(
		DetailClassification detailClassification, int page, int size) {

		Pageable pageable = PageRequest.of(page - 1, size,
			Sort.by(Sort.Order.asc("status"), Sort.Order.asc("startTime")));

		Page<PublicSchedule> publicSchedules = publicScheduleAdminRepository.findAllByClassification(
			detailClassification, pageable);

		List<PublicScheduleAdminResDto> publicScheduleList = publicSchedules.stream()
			.map(PublicScheduleAdminResDto::new)
			.collect(Collectors.toList());
		return PageResponseDto.of(publicSchedules.getTotalElements(), publicScheduleList);
	}

	private void dateTimeErrorCheck(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}

}
