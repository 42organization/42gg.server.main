package gg.calendar.api.admin;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.TechTag;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.CustomRuntimeException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicScheduleMockData {

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
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(10))
			.build();

		return publicScheduleAdminRepository.save(publicSchedule);
	}

	public PublicScheduleAdminCreateReqDto createPublicScheduleAdminCreateReqDto() {
		return PublicScheduleAdminCreateReqDto.builder()
			.detailClassification(DetailClassification.EVENT.getValue())
			.eventTag(EventTag.JOB_FORUM.getValue())
			.title("취업설명회")
			.content("취업설명회입니다.")
			.link("https://gg.42seoul.kr")
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.now().plusDays(10))
			.build();
	}


}
