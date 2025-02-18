package gg.calendar.api.user.schedule.publicschedule.controller.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import gg.utils.annotation.UnitTest;

@UnitTest
public class TotalScheduleRetrieveResDtoTest {

	@Test
	@DisplayName("TotalScheduleRetrieveResDto 생성자 테스트")
	void toDtoSuccess() {
		// given
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicSchedule schedule = PublicSchedule.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.jobTag(JobTag.EXPERIENCED)
			.techTag(TechTag.NETWORK)
			.author("testUser")
			.title("Test Title")
			.content("Test Content")
			.link("https://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.status(ScheduleStatus.ACTIVATE)
			.build();
		ReflectionTestUtils.setField(schedule, "id", 1L);

		// when
		TotalScheduleRetrieveResDto responseDto = TotalScheduleRetrieveResDto.toDto(schedule);
		//then
		assertAll(() -> assertEquals(1L, responseDto.getId()),
			() -> assertEquals(DetailClassification.JOB_NOTICE, responseDto.getClassification()),
			() -> assertEquals(JobTag.EXPERIENCED, responseDto.getJobTag()),
			() -> assertEquals(TechTag.NETWORK, responseDto.getTechTag()),
			() -> assertEquals("testUser", responseDto.getAuthor()),
			() -> assertEquals("Test Title", responseDto.getTitle()),
			() -> assertEquals("Test Content", responseDto.getContent()),
			() -> assertEquals("https://test.com", responseDto.getLink()),
			() -> assertEquals(startTime.toString(), responseDto.getStartTime()),
			() -> assertEquals(endTime.toString(), responseDto.getEndTime()),
			() -> assertEquals("ACTIVATE", responseDto.getStatus()));
	}
}
