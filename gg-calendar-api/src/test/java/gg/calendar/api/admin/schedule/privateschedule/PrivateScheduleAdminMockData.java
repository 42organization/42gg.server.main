package gg.calendar.api.admin.schedule.privateschedule;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.ScheduleGroup;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.calendar.ScheduleGroupRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PrivateScheduleAdminMockData {

	private final PublicScheduleRepository publicScheduleRepository;
	private final ScheduleGroupRepository scheduleGroupRepository;
	private final PrivateScheduleRepository privateScheduleRepository;

	public PublicSchedule createPublicSchedule(String author) {
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.EVENT)
			.eventTag(null)
			.jobTag(null)
			.techTag(null)
			.title("Test Schedule")
			.author(author)
			.content("Test Content")
			.link("http://test.com")
			.status(ScheduleStatus.ACTIVATE)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(1))
			.build();
		return publicScheduleRepository.save(publicSchedule);
	}

	public PublicSchedule createPublicPrivateSchedule(String author) {
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.PRIVATE_SCHEDULE)
			.eventTag(null)
			.jobTag(null)
			.techTag(null)
			.title("Test Schedule")
			.author(author)
			.content("Test Content")
			.link("http://test.com")
			.status(ScheduleStatus.ACTIVATE)
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

	public PrivateSchedule createPrivateSchedule(PublicSchedule publicSchedule, ScheduleGroup scheduleGroup) {
		PrivateSchedule privateSchedule = new PrivateSchedule(scheduleGroup.getUser(), publicSchedule, false,
			scheduleGroup.getId());
		return privateScheduleRepository.save(privateSchedule);
	}

	public PrivateSchedule createPrivateScheduleNoGroup(PublicSchedule publicSchedule, User user) {
		PrivateSchedule privateSchedule = new PrivateSchedule(user, publicSchedule, false,
			500L);
		return privateScheduleRepository.save(privateSchedule);
	}
}
