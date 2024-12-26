package gg.calendar.api.user.schedule.publicschedule;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import gg.data.BaseTimeEntity;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.repo.calendar.PublicScheduleRepository;
import gg.utils.TestDataUtils;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicScheduleMockData {
	private final EntityManager em;
	private final PublicScheduleRepository publicScheduleRepository;
	private final TestDataUtils testDataUtils;


	public PublicSchedule createPublicSchedule(String author) {
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.EVENT)
			.eventTag(EventTag.NONE)
			.jobTag(null)
			.techTag(null)
			.title("Test Schedule")
			.status(ScheduleStatus.ACTIVATE)
			.author(author)
			.content("Test Content")
			.link("http://test.com")
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(1))
			.build();
		return publicScheduleRepository.save(publicSchedule);
	}
}
