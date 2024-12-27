package gg.calendar.api.user.schedule.publicschedule.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.CustomRuntimeException;
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
		if (req.getStartTime().isAfter(req.getEndTime())) {
			throw new CustomRuntimeException(ErrorCode.CALENDAR_BEFORE_DATE);
		}
		PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(), req);
		publicScheduleRepository.save(publicSchedule);
	}
}
