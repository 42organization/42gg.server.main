package gg.calendar.api.admin.schedule.publicschedule.service;



import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.TechTag;
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

		dateTimeErrorCheck(publicScheduleAdminCreateReqDto.getStartTime(), publicScheduleAdminCreateReqDto.getEndTime());

		PublicSchedule publicSchedule = PublicScheduleAdminCreateReqDto.toEntity(publicScheduleAdminCreateReqDto);
		publicScheduleAdminRepository.save(publicSchedule);
	}

	private void dateTimeErrorCheck(LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime)) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}


}
