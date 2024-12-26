package gg.calendar.api.admin.schedule.publicschedule.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.PublicScheduleAdminMockData;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
public class PublicScheduleAdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	EntityManager em;

	@Autowired
	private PublicScheduleAdminMockData publicScheduleAdminMockData;

	@Autowired
	private PublicScheduleAdminRepository publicScheduleAdminRepository;

	@Autowired
	private TestDataUtils testDataUtils;

	private User user;

	private String accessToken;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createAdminUser();
		accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Nested
	@DisplayName("Admin PublicSchedule 등록 테스트")
	// mockMvc.perform()로 테스트 -> 실제 API 호출
	class createPublicScheduleTest {

		@Autowired
		private PublicScheduleAdminService publicScheduleAdminService;

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 성공")
		void createPublicScheduleTestSuccess() throws Exception {
			// given
			PublicScheduleAdminCreateReqDto publicScheduleAdminReqDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.EVENT)
				.eventTag(EventTag.JOB_FORUM)
				.title("취업설명회")
				.content("취업설명회입니다.")
				.link("https://gg.42seoul.kr")
				.status(ScheduleStatus.ACTIVATE)
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10))
				.build();

			// when
			mockMvc.perform(post("/admin/calendar/public")
				.header("Authorization", "Bearer " + accessToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(publicScheduleAdminReqDto)))
				.andDo(print())
				.andExpect(status().isCreated());

			// then
			List<PublicSchedule> schedules = publicScheduleAdminRepository.findByAuthor("42GG");
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo(publicScheduleAdminReqDto.getTitle());
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 종료날짜가 시작날짜보다 빠른경우")
		public void createPublicScheduleWrongDateTime() throws Exception {
			try {
				PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
					.detailClassification(DetailClassification.EVENT).eventTag(EventTag.JOB_FORUM).title("취업설명회")
					.content("취업설명회입니다.").status(ScheduleStatus.ACTIVATE)
					.link("https://gg.42seoul.kr").startTime(LocalDateTime.now().plusDays(10))
					.endTime(LocalDateTime.now()).build();
				publicScheduleAdminService.createPublicSchedule(requestDto);
			} catch (Exception e) {
				assertThat(e.getMessage()).isEqualTo("종료 시간이 시작 시간보다 빠를 수 없습니다.");
			}
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 제목이 50자가 넘는경우")
		public void createPublicScheduleTitleMax() throws Exception {

			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.JOB_NOTICE).jobTag(JobTag.SHORTS_INTERN)
				.title("TEST".repeat(13)).content("취업설명회입니다.").status(ScheduleStatus.ACTIVATE)
				.link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10)).build();

			mockMvc.perform(post("/admin/calendar/public")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 내용이 2000자가 넘는경우")
		public void createPublicScheduleContentMax() throws Exception {

			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.JOB_NOTICE).jobTag(JobTag.SHORTS_INTERN)
				.title("취업설명회").content("취업설명회".repeat(401)).status(ScheduleStatus.ACTIVATE)
				.link("https://gg.42seoul.kr").startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10)).build();

			mockMvc.perform(post("/admin/calendar/public")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andDo(print())
				.andExpect(status().isBadRequest());
		}
	}
}
