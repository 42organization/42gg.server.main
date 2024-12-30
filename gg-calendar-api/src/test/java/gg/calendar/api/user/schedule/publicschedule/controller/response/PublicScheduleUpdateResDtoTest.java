package gg.calendar.api.user.schedule.publicschedule.controller.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PublicScheduleUpdateResDtoTest {

	@Test
	@DisplayName("PublicSchedule Entity를 ResponseDto로 변환 성공")
	void toDto_Success() {
		// given
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicSchedule schedule = PublicSchedule.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.eventTag(EventTag.INSTRUCTION)
			.author("testUser")
			.title("Test Title")
			.content("Test Content")
			.link("http://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.status(ScheduleStatus.ACTIVATE)
			.build();
		ReflectionTestUtils.setField(schedule, "id", 1L);
		// when
		PublicScheduleUpdateResDto responseDto = PublicScheduleUpdateResDto.toDto(schedule);

		// then
		assertAll(
			() -> assertEquals(1L, responseDto.getId()),
			() -> assertEquals(DetailClassification.JOB_NOTICE, responseDto.getClassification()),
			() -> assertEquals(EventTag.INSTRUCTION, responseDto.getEventTag()),
			() -> assertEquals("testUser", responseDto.getAuthor()),
			() -> assertEquals("Test Title", responseDto.getTitle()),
			() -> assertEquals("Test Content", responseDto.getContent()),
			() -> assertEquals("http://test.com", responseDto.getLink()),
			() -> assertEquals(startTime.toString(), responseDto.getStartTime()),
			() -> assertEquals(endTime.toString(), responseDto.getEndTime()),
			() -> assertEquals("ACTIVATE", responseDto.getStatus())
		);
	}
}
