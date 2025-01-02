package gg.calendar.api.admin.schedule.publicschedule.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateEventReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminUpdateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminResDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminUpdateResDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.ScheduleStatus;
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
	public void createPublicScheduleEvent(PublicScheduleAdminCreateEventReqDto publicScheduleAdminCreateEventReqDto) {

		dateTimeErrorCheck(publicScheduleAdminCreateEventReqDto.getStartTime(),
			publicScheduleAdminCreateEventReqDto.getEndTime());
		PublicSchedule publicSchedule = PublicScheduleAdminCreateEventReqDto.toEntity(
			publicScheduleAdminCreateEventReqDto);
		publicScheduleAdminRepository.save(publicSchedule);
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
			publicScheduleAdminUpdateReqDto.getStartTime(), publicScheduleAdminUpdateReqDto.getEndTime());

		return PublicScheduleAdminUpdateResDto.toDto(publicSchedule);
	}

	@Transactional
	public void deletePublicSchedule(Long id) {
		PublicSchedule publicSchedule = publicScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		isDeleted(publicSchedule);
		publicSchedule.delete();
	}

	public PublicScheduleAdminResDto detailPublicSchedule(Long id) {
		PublicSchedule publicSchedule = publicScheduleAdminRepository.findById(id)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));

		return new PublicScheduleAdminResDto(publicSchedule);
	}

	private void dateTimeErrorCheck(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}

	private void isDeleted(PublicSchedule publicSchedule) {
		if (publicSchedule.getStatus().equals(ScheduleStatus.DELETE)) {
			throw new InvalidParameterException(ErrorCode.PUBLIC_SCHEDULE_ALREADY_DELETED);
		}
	}

}
