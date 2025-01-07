package gg.calendar.api.user.schedule.publicschedule;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.repo.calendar.PublicScheduleRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicScheduleMockData {
	private final PublicScheduleRepository publicScheduleRepository;

	public void createPublicScheduleEvent(int size) {
		for (int i = 0; i < size; i++) {
			PublicSchedule publicSchedule = PublicSchedule.builder()
				.classification(DetailClassification.EVENT)
				.eventTag(EventTag.JOB_FORUM)
				.author("42GG")
				.title("Job " + i)
				.content("TEST EVENT")
				.link("https://gg.42seoul.kr")
				.status(ScheduleStatus.ACTIVATE)
				.startTime(LocalDateTime.now().plusDays(i))
				.endTime(LocalDateTime.now().plusDays(i + 10))
				.build();
			publicScheduleRepository.save(publicSchedule);
		}
	}

	public void createPublicScheduleJob(int size) {
		for (int i = 0; i < size; i++) {
			PublicSchedule publicSchedule = PublicSchedule.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.EXPERIENCED)
				.author("42GG")
				.title("Job " + i)
				.content("TEST JOB")
				.link("https://gg.42seoul.kr")
				.status(ScheduleStatus.ACTIVATE)
				.startTime(LocalDateTime.now().plusDays(i))
				.endTime(LocalDateTime.now().plusDays(i + 10))
				.build();
			publicScheduleRepository.save(publicSchedule);
		}
	}
}
