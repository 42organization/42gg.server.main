package gg.calendar.api.user.custom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.auth.UserDto;
import gg.calendar.api.user.custom.controller.request.CalendarCustomCreateReqDto;
import gg.data.calendar.ScheduleGroup;
import gg.data.user.User;
import gg.repo.calendar.ScheduleGroupRepository;
import gg.repo.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CalendarCustomService {
	private final ScheduleGroupRepository scheduleGroupRepository;
	private final UserRepository userRepository;

	@Transactional
	public void createScheduleGroup(UserDto userDto, CalendarCustomCreateReqDto calendarCustomCreateReqDto) {
		User user = userRepository.getById(userDto.getId());
		ScheduleGroup scheduleGroup = CalendarCustomCreateReqDto.toEntity(user, calendarCustomCreateReqDto);
		scheduleGroupRepository.save(scheduleGroup);
	}
}
