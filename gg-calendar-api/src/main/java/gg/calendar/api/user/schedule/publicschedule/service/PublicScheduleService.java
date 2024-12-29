package gg.calendar.api.user.schedule.publicschedule.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.CustomRuntimeException;
import gg.utils.exception.custom.InvalidParameterException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createPublicSchedule(PublicScheduleCreateReqDto req, Long userId) {
		User user = userRepository.getById(userId);
		if (!user.getIntraId().equals(req.getAuthor())) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_AUTHOR_NOT_MATCH);
		}
		validateTimeRange(req.getStartTime(), req.getEndTime());

		PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(), req);
		publicScheduleRepository.save(publicSchedule);
	}

	@Transactional
	public void updatePublicSchedule(Long scheduleId, PublicScheduleUpdateReqDto req, Long userId) {
		User user = userRepository.getById(userId);

		PublicSchedule existingSchedule = publicScheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new CustomRuntimeException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		if (!existingSchedule.getAuthor().equals(user.getIntraId())) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_AUTHOR_NOT_MATCH);
		}

		if (req.getStartTime().isAfter(req.getEndTime())) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
		existingSchedule.publicScheduleUpdate(req.getClassification(), req.getEventTag(), req.getJobTag(),
			req.getTechTag(), req.getTitle(), req.getContent(), req.getLink(), req.getStartTime(), req.getEndTime());
	}

	public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
		if (endTime.isBefore(startTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}
}
