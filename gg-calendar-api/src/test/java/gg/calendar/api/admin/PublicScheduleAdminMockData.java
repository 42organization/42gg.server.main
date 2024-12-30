package gg.calendar.api.admin;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicScheduleAdminMockData {

	private final EntityManager em;
	private final PublicScheduleAdminRepository publicScheduleAdminRepository;

	public PublicSchedule createPublicSchedule() {
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.EVENT)
			.eventTag(EventTag.JOB_FORUM)
			.author("42GG")
			.title("취업설명회")
			.content("취업설명회입니다.")
			.link("https://gg.42seoul.kr")
			.status(ScheduleStatus.ACTIVATE)
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(10))
			.build();

		return publicScheduleAdminRepository.save(publicSchedule);
	}

	public void createPublicScheduleEvent(int size) {
		for (int i = 0; i < size; i++) {
			PublicSchedule publicSchedule = PublicSchedule.builder()
				.classification(DetailClassification.EVENT)
				.eventTag(EventTag.JOB_FORUM)
				.author("42GG")
				.title("Job " + i)
				.content("TEST JOB")
				.link("https://gg.42seoul.kr")
				.status(ScheduleStatus.ACTIVATE)
				.startTime(LocalDateTime.now().plusDays(i))
				.endTime(LocalDateTime.now().plusDays(i + 10))
				.build();
			publicScheduleAdminRepository.save(publicSchedule);
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
			publicScheduleAdminRepository.save(publicSchedule);
		}
	}

	public void createPublicSchedulePrivate(int size) {
		for (int i = 0; i < size; i++) {
			PublicSchedule publicSchedule = PublicSchedule.builder()
				.classification(DetailClassification.PRIVATE_SCHEDULE)
				.author("42GG")
				.title("Private " + i)
				.content("TEST Private")
				.link("https://gg.42seoul.kr")
				.status(ScheduleStatus.ACTIVATE)
				.startTime(LocalDateTime.now().plusDays(i))
				.endTime(LocalDateTime.now().plusDays(i + 10))
				.build();
			publicScheduleAdminRepository.save(publicSchedule);
		}
	}
}
