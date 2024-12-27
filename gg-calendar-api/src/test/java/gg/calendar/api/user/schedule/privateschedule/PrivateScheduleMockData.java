package gg.calendar.api.user.schedule.privateschedule;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.ScheduleGroup;
import gg.data.calendar.type.DetailClassification;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.calendar.ScheduleGroupRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PrivateScheduleMockData {
	private final PublicScheduleRepository publicScheduleRepository;
	private final ScheduleGroupRepository scheduleGroupRepository;

	public PublicSchedule createPublicSchedule() {
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.EVENT)
			.eventTag(null)
			.jobTag(null)
			.techTag(null)
			.title("Test Schedule")
			.author("author")
			.content("Test Content")
			.link("http://test.com")
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(1))
			.build();
		return publicScheduleRepository.save(publicSchedule);
	}

	public ScheduleGroup createScheduleGroup(User user) {
		ScheduleGroup scheduleGroup = ScheduleGroup.builder()
			.user(user)
			.title("title")
			.backgroundColor("")
			.build();
		return scheduleGroupRepository.save(scheduleGroup);
	}
}
