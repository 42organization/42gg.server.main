package gg.calendar.api.user.custom.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.repo.calendar.ScheduleGroupRepository;
import lombok.RequiredArgsConstructor;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class CalendarCustomService {
	private final ScheduleGroupRepository scheduleGroupRepository;
}
