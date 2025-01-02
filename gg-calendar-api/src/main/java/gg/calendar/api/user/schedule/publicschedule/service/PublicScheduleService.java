package gg.calendar.api.user.schedule.publicschedule.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.ForbiddenException;
import gg.utils.exception.custom.InvalidParameterException;
import gg.utils.exception.custom.NotExistException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicScheduleService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final UserRepository userRepository;
	private final PrivateScheduleRepository privateScheduleRepository;

	@Transactional
	public void createPublicSchedule(PublicScheduleCreateReqDto req, Long userId) {
		User user = userRepository.getById(userId);
		if (!user.getIntraId().equals(req.getAuthor())) {
			throw new ForbiddenException(ErrorCode.CALENDAR_AUTHOR_NOT_MATCH);
		}
		validateTimeRange(req.getStartTime(), req.getEndTime());
		PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(), req);
		publicScheduleRepository.save(publicSchedule);
	}

	@Transactional
	public PublicSchedule updatePublicSchedule(Long scheduleId, PublicScheduleUpdateReqDto req, Long userId) {
		User user = userRepository.getById(userId);
		PublicSchedule existingSchedule = publicScheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		checkAuthor(existingSchedule.getAuthor(), user);
		checkAuthor(req.getAuthor(), user);
		validateTimeRange(req.getStartTime(), req.getEndTime());
		existingSchedule.update(req.getClassification(), req.getEventTag(), req.getJobTag(), req.getTechTag(),
			req.getTitle(), req.getContent(), req.getLink(), req.getStartTime(), req.getEndTime());
		return existingSchedule;
	}

	@Transactional
	public void deletePublicSchedule(Long scheduleId, Long userId) {
		User user = userRepository.getById(userId);
		PublicSchedule existingSchedule = publicScheduleRepository.findById(scheduleId)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		checkAuthor(existingSchedule.getAuthor(), user);
		List<PrivateSchedule> privateSchedules = privateScheduleRepository.findByPublicSchedule(existingSchedule);
		existingSchedule.delete();
		if (!privateSchedules.isEmpty()) {
			for (PrivateSchedule privateSchedule : privateSchedules) {
				privateSchedule.delete();
			}
		}
	}

	public PublicSchedule getPublicScheduleDetailRetrive(Long sheduleId, Long userId) {
		User user = userRepository.getById(userId);
		PublicSchedule publicRetriveSchedule = publicScheduleRepository.findById(sheduleId)
			.orElseThrow(() -> new NotExistException(ErrorCode.PUBLIC_SCHEDULE_NOT_FOUND));
		checkAuthor(publicRetriveSchedule.getAuthor(), user);
		return publicRetriveSchedule;
	}

	private void checkAuthor(String author, User user) {
		if (!user.getIntraId().equals(author)) {
			throw new ForbiddenException(ErrorCode.CALENDAR_AUTHOR_NOT_MATCH);
		}
	}

	public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
		if (endTime.isBefore(startTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}
}
