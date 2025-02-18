package gg.calendar.api.user.schedule.publicschedule.controller.response;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PublicScheduleDetailRetrieveResDtoTest {

	@Test
	@DisplayName("PublicSchedule Entity를 ResponseDto로 변환 성공")
	void toDto_Success() {
		//given
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicSchedule schedule = PublicSchedule.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.eventTag(EventTag.INSTRUCTION)
			.author("testUser")
			.title("Test Title")
			.content("Test Content")
			.link("https://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.build();

		//when
		PublicScheduleDetailRetrieveResDto responseDto = PublicScheduleDetailRetrieveResDto.toDto(schedule);

		//then
		assertAll(
			() -> assertEquals(null, responseDto.getId()),
			() -> assertEquals(DetailClassification.JOB_NOTICE, responseDto.getClassification()),
			() -> assertEquals(EventTag.INSTRUCTION, responseDto.getEventTag()),
			() -> assertEquals("testUser", responseDto.getAuthor()),
			() -> assertEquals("Test Title", responseDto.getTitle()),
			() -> assertEquals("Test Content", responseDto.getContent()),
			() -> assertEquals("https://test.com", responseDto.getLink()),
			() -> assertEquals(startTime.toString(), responseDto.getStartTime()),
			() -> assertEquals(endTime.toString(), responseDto.getEndTime()),
			() -> assertEquals(schedule.getSharedCount(), responseDto.getSharedCount())
		);
	}

}
