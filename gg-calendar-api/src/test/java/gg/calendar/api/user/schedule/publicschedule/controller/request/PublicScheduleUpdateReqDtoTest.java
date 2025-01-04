package gg.calendar.api.user.schedule.publicschedule.controller.request;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PublicScheduleUpdateReqDtoTest {

	@Test
	@DisplayName("PublicScheduleUpdateReqDto 생성성공(JOB_NOTICE)")
	void createPublicScheduleSuccess() {
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicScheduleUpdateReqDto dto = PublicScheduleUpdateReqDto.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.jobTag(JobTag.EXPERIENCED)
			.title("Test Schedule")
			.author("intraId")
			.content("Test Content")
			.link("https://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.build();

		assertAll(() -> assertEquals(DetailClassification.JOB_NOTICE, dto.getClassification()),
			() -> assertEquals(JobTag.EXPERIENCED, dto.getJobTag()),
			() -> assertEquals("Test Schedule", dto.getTitle()), () -> assertEquals("intraId", dto.getAuthor()),
			() -> assertEquals("Test Content", dto.getContent()), () -> assertEquals("https://test.com", dto.getLink()),
			() -> assertEquals(startTime, dto.getStartTime()), () -> assertEquals(endTime, dto.getEndTime()));
	}

	@Test
	@DisplayName("PublicScheduleUpdateReqDto validation test(JOB_NOTICE eventTag 에 값이 들어간 경우)")
	void validatePublicScheduleUpdateReqDto() {
		//given
		LocalDateTime startTime = LocalDateTime.now().plusDays(1);
		LocalDateTime endTime = LocalDateTime.now().plusDays(2);

		PublicScheduleUpdateReqDto dto = PublicScheduleUpdateReqDto.builder()
			.classification(DetailClassification.JOB_NOTICE)
			.eventTag(EventTag.INSTRUCTION)
			.title("Test Schedule")
			.author("intraId")
			.content("Test Content")
			.link("https://test.com")
			.startTime(startTime)
			.endTime(endTime)
			.build();
		//when & then
		try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<PublicScheduleUpdateReqDto>> violations = validator.validate(dto);

			assertFalse(violations.isEmpty());
			assertEquals("classification must match with eventTag, jobTag, techTag",
				violations.iterator().next().getMessage());
		}
	}
}
