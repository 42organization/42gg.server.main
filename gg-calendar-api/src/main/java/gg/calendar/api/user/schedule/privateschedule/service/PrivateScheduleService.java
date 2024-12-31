package gg.calendar.api.user.schedule.privateschedule.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.auth.UserDto;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleUpdateReqDto;
import gg.calendar.api.user.schedule.privateschedule.controller.response.PrivateScheduleUpdateResDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.ScheduleGroup;
import gg.data.user.User;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.calendar.ScheduleGroupRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.ForbiddenException;
import gg.utils.exception.custom.InvalidParameterException;
import gg.utils.exception.custom.NotExistException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PrivateScheduleService {
	private final PrivateScheduleRepository privateScheduleRepository;
	private final PublicScheduleRepository publicScheduleRepository;
	private final ScheduleGroupRepository scheduleGroupRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createPrivateSchedule(UserDto userDto, PrivateScheduleCreateReqDto privateScheduleCreateReqDto) {
		validateTimeRange(privateScheduleCreateReqDto.getStartTime(), privateScheduleCreateReqDto.getEndTime());
		PublicSchedule publicSchedule = PrivateScheduleCreateReqDto.toEntity(userDto.getIntraId(),
			privateScheduleCreateReqDto);
		publicScheduleRepository.save(publicSchedule);
		ScheduleGroup scheduleGroup = scheduleGroupRepository.findById(privateScheduleCreateReqDto.getGroupId())
			.orElseThrow(() -> new NotExistException(ErrorCode.SCHEDULE_GROUP_NOT_FOUND));
		User user = userRepository.getById(userDto.getId());
		PrivateSchedule privateSchedule = new PrivateSchedule(user, publicSchedule,
			privateScheduleCreateReqDto.isAlarm(), scheduleGroup.getId());
		privateScheduleRepository.save(privateSchedule);
	}

	@Transactional
	public PrivateScheduleUpdateResDto updatePrivateSchedule(UserDto userDto,
		PrivateScheduleUpdateReqDto privateScheduleUpdateReqDto, Long privateScheduleId) {
		validateTimeRange(privateScheduleUpdateReqDto.getStartTime(), privateScheduleUpdateReqDto.getEndTime());
		PrivateSchedule privateSchedule = privateScheduleRepository.findById(privateScheduleId)
			.orElseThrow(() -> new NotExistException(ErrorCode.PRIVATE_SCHEDULE_NOT_FOUND));
		validateAuthor(userDto.getIntraId(), privateSchedule.getPublicSchedule().getAuthor());
		scheduleGroupRepository.findById(privateScheduleUpdateReqDto.getGroupId())
			.orElseThrow(() -> new NotExistException(ErrorCode.SCHEDULE_GROUP_NOT_FOUND));

		privateSchedule.update(privateScheduleUpdateReqDto.getEventTag(), privateScheduleUpdateReqDto.getJobTag(),
			privateScheduleUpdateReqDto.getTechTag(), privateScheduleUpdateReqDto.getTitle(),
			privateScheduleUpdateReqDto.getContent(), privateScheduleUpdateReqDto.getLink(),
			privateScheduleUpdateReqDto.getStatus(), privateScheduleUpdateReqDto.getStartTime(),
			privateScheduleUpdateReqDto.getEndTime(), privateScheduleUpdateReqDto.isAlarm(),
			privateScheduleUpdateReqDto.getGroupId());
		return PrivateScheduleUpdateResDto.toDto(privateSchedule);
	}

	@Transactional
	public void deletePrivateSchedule(UserDto userDto, Long privateScheduleId) {
		PrivateSchedule privateSchedule = privateScheduleRepository.findById(privateScheduleId)
			.orElseThrow(() -> new NotExistException(ErrorCode.PRIVATE_SCHEDULE_NOT_FOUND));
		privateSchedule.delete();
	}

	public void validateTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
		if (endTime.isBefore(startTime)) {
			throw new InvalidParameterException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
	}

	public void validateAuthor(String intraId, String author) {
		if (!intraId.equals(author)) {
			throw new ForbiddenException(ErrorCode.CALENDAR_AUTHOR_NOT_MATCH);
		}
	}
}
