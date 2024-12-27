package gg.calendar.api.admin.schedule.publicschedule.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PublicScheduleAdminServiceTest {

	@Mock
	PublicScheduleAdminRepository publicScheduleAdminRepository;

	@InjectMocks
	PublicScheduleAdminService publicScheduleAdminService;

	@Nested
	@DisplayName("Admin PublicSchedule 등록 테스트")
	class CreateAdminPublicScheduleTest {

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 성공")
		public void createPublicScheduleTest() {
			// Given
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
			when(publicScheduleAdminRepository.save(any())).thenReturn(publicSchedule);
			// When
			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.EVENT)
				.eventTag(EventTag.JOB_FORUM)
				.title("취업설명회")
				.content("취업설명회입니다.")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10))
				.build();
			publicScheduleAdminService.createPublicSchedule(requestDto);
			// Then
			verify(publicScheduleAdminRepository, times(1)).save(any(PublicSchedule.class));
			assertThat(DetailClassification.EVENT).isEqualTo(publicSchedule.getClassification());
			assertThat(EventTag.JOB_FORUM).isEqualTo(publicSchedule.getEventTag());
			assertThat("취업설명회").isEqualTo(publicSchedule.getTitle());
			assertThat("취업설명회입니다.").isEqualTo(publicSchedule.getContent());
			assertThat("https://gg.42seoul.kr").isEqualTo(publicSchedule.getLink());
			assertNotNull(publicSchedule.getStartTime());
			assertNotNull(publicSchedule.getEndTime());

		}
	}
}
