package gg.calendar.api.user.schedule.publicschedule.controller.request;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PublicScheduleUpdateReqDtoTest {

	@Test
	@DisplayName("PublicScheduleUpdateReqDto 생성 성공")
	void createPublicScheduleSuccess() {
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicScheduleUpdateReqDto dto = PublicScheduleUpdateReqDto.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.eventTag(EventTag.INSTRUCTION)
			.title("Test Schedule")
			.author("intraId")
			.content("Test Content")
			.link("http://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.build();

		assertAll(() -> assertEquals(DetailClassification.JOB_NOTICE, dto.getClassification()),
			() -> assertEquals(EventTag.INSTRUCTION, dto.getEventTag()),
			() -> assertEquals("Test Schedule", dto.getTitle()), () -> assertEquals("intraId", dto.getAuthor()),
			() -> assertEquals("Test Content", dto.getContent()), () -> assertEquals("http://test.com", dto.getLink()),
			() -> assertEquals(startTime, dto.getStartTime()), () -> assertEquals(endTime, dto.getEndTime()));

	}
}
