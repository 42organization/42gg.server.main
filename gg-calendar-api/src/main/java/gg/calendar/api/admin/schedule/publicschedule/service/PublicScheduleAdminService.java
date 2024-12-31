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
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminUpdateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminResDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminUpdateResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.utils.dto.PageResponseDto;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.InvalidParameterException;
import gg.utils.exception.custom.NotExistException;
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

	public PageResponseDto<PublicScheduleAdminResDto> findAllByClassification(DetailClassification detailClassification,
		int page, int size) {

		Pageable pageable = PageRequest.of(page - 1, size,
			Sort.by(Sort.Order.asc("status"), Sort.Order.asc("startTime")));

		Page<PublicSchedule> publicSchedules = publicScheduleAdminRepository.findAllByClassification(
			detailClassification, pageable);

		List<PublicScheduleAdminResDto> publicScheduleList = publicSchedules.stream()
			.map(PublicScheduleAdminResDto::new)
			.collect(Collectors.toList());
		return PageResponseDto.of(publicSchedules.getTotalElements(), publicScheduleList);
	}

	@Transactional
	public PublicScheduleAdminUpdateResDto updatePublicSchedule(
		PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto, Long id) {
		dateTimeErrorCheck(publicScheduleAdminUpdateReqDto.getStartTime(),
			publicScheduleAdminUpdateReqDto.getEndTime());

		PublicSchedule publicSchedule = publicScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));

		publicSchedule.update(publicScheduleAdminUpdateReqDto.getClassification(),
			publicScheduleAdminUpdateReqDto.getEventTag(), publicScheduleAdminUpdateReqDto.getJobTag(),
			publicScheduleAdminUpdateReqDto.getTechTag(), publicScheduleAdminUpdateReqDto.getTitle(),
			publicScheduleAdminUpdateReqDto.getContent(), publicScheduleAdminUpdateReqDto.getLink(),
			publicScheduleAdminUpdateReqDto.getStartTime(), publicScheduleAdminUpdateReqDto.getEndTime(),
			publicScheduleAdminUpdateReqDto.getStatus());

		return PublicScheduleAdminUpdateResDto.toDto(publicSchedule);
	}

	@Transactional
	public void deletePublicSchedule(Long id) {
		PublicSchedule publicSchedule = publicScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		publicSchedule.delete();
	}

	private void dateTimeErrorCheck(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}

}
