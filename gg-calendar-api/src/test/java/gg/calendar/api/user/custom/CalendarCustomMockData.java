package gg.calendar.api.user.custom;

import org.springframework.stereotype.Component;

import gg.data.calendar.ScheduleGroup;
import gg.data.user.User;
import gg.repo.calendar.ScheduleGroupRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CalendarCustomMockData {
	private final ScheduleGroupRepository scheduleGroupRepository;

	public ScheduleGroup createScheduleGroup(User user) {
		ScheduleGroup scheduleGroup = ScheduleGroup.builder()
			.user(user)
			.title("title")
			.backgroundColor("#FFFFFF")
			.build();
		return scheduleGroupRepository.save(scheduleGroup);
	}
}
