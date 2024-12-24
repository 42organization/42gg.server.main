package gg.calendar.api.admin.schedule.publicschedule.Service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.utils.annotation.UnitTest;
import gg.utils.exception.custom.CustomRuntimeException;

@UnitTest
public class PublicScheduleAdminServiceTest {

	@Mock
	PublicScheduleAdminRepository publicScheduleAdminRepository;

	@InjectMocks
	PublicScheduleAdminService publicScheduleAdminService;

	@Nested
	@DisplayName("Admin PublicSchedule 등록 테스트")
	class createAdminPublicScheduleTest {

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 성공")
		public void createPublicScheduleTest() {
			// Given
			PublicSchedule publicSchedule = PublicSchedule.builder().classification(DetailClassification.EVENT)
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
				.detailClassification(DetailClassification.EVENT.getValue()).eventTag(EventTag.JOB_FORUM.getValue()).title("취업설명회")
				.content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10)).build();
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

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 종료날짜가 시작날짜보다 빠른경우")
		public void createPublicScheduleWrongDateTime() throws Exception {
			try {
				PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
					.detailClassification(DetailClassification.EVENT.getValue()).eventTag(EventTag.JOB_FORUM.getValue()).title("취업설명회")
					.content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now().plusDays(10))
					.endTime(LocalDateTime.now()).build();
				publicScheduleAdminService.createPublicSchedule(requestDto);
			} catch (Exception e) {
				assertThat(e.getMessage()).isEqualTo("종료 시간이 시작 시간보다 빠를 수 없습니다.");
			}
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 없는 DetailClassification 값을 입력한경우")
		public void createPublicScheduleWrongEnum() throws Exception {

			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification("TEST").eventTag(EventTag.JOB_FORUM.getValue()).title("취업설명회")
				.content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10)).build();

			Exception exception = assertThrows(CustomRuntimeException.class, () -> {
				publicScheduleAdminService.createPublicSchedule(requestDto);
			});

			assertThat(exception.getMessage()).isEqualTo("잘못된 argument 입니다.");
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 없는 EVENT_TAG 값을 입력한경우")
		public void createPublicScheduleWrongEnum2() throws Exception {

			Exception exception = assertThrows(CustomRuntimeException.class, () -> {
				PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
					.detailClassification(DetailClassification.EVENT.getValue()).eventTag("TEST").title("취업설명회")
					.content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(10)).build();
			});

			assertThat(exception.getMessage()).isEqualTo("잘못된 argument 입니다.");
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 없는 JOB_TAG 값을 입력한경우")
		public void createPublicScheduleWrongEnum3() throws Exception {

			Exception exception = assertThrows(CustomRuntimeException.class, () -> {
				PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
					.detailClassification(DetailClassification.JOB_NOTICE.getValue()).jobTag("TEST").title("취업설명회")
					.content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(10)).build();
			});

			assertThat(exception.getMessage()).isEqualTo("잘못된 argument 입니다.");
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 없는 TECH_TAG 값을 입력한경우")
		public void createPublicScheduleWrongEnum4() throws Exception {

			Exception exception = assertThrows(CustomRuntimeException.class, () -> {
				PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
					.detailClassification(DetailClassification.JOB_NOTICE.getValue()).jobTag(JobTag.SHORTS_INTERN.getValue())
					.techTag("TEST").title("취업설명회").content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(10)).build();
			});

			assertThat(exception.getMessage()).isEqualTo("잘못된 argument 입니다.");
		}

		@Nested
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패")
		class createAdminPublicScheduleValidTest {

			@Test
			@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 제목이 50자가 넘는경우")
			public void createPublicScheduleTitleMax() throws Exception {
				try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
					Validator validator = factory.getValidator();

					PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
						.detailClassification(DetailClassification.JOB_NOTICE.getValue()).jobTag(JobTag.SHORTS_INTERN.getValue())
						.title("TEST".repeat(13)).
						content("취업설명회입니다.").link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
						.endTime(LocalDateTime.now().plusDays(10)).build();

					Set<ConstraintViolation<PublicScheduleAdminCreateReqDto>> violations = validator.validate(requestDto);
					assertThat(violations).hasSize(1);
					assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("title");
				}
			}

			@Test
			@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 내용이 2000자가 넘는경우")
			public void createPublicScheduleContentMax() throws Exception {
				try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
					Validator validator = factory.getValidator();
					PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
						.detailClassification(DetailClassification.JOB_NOTICE.getValue()).jobTag(JobTag.SHORTS_INTERN.getValue())
						.title("취업설명회")
						.content("취업설명회".repeat(401)).link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
						.endTime(LocalDateTime.now().plusDays(10)).build();

					Set<ConstraintViolation<PublicScheduleAdminCreateReqDto>> violations = validator.validate(requestDto);
					assertThat(violations).hasSize(1);
					assertThat(violations.iterator().next().getPropertyPath().toString()).isEqualTo("content");
				}

			}
		}

	}
}
