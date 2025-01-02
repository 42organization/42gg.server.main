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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gg.admin.repo.calendar.PublicScheduleAdminRepository;
import gg.calendar.api.admin.PublicScheduleAdminMockData;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminUpdateReqDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminResDto;
import gg.calendar.api.admin.schedule.publicschedule.controller.response.PublicScheduleAdminUpdateResDto;
import gg.calendar.api.admin.schedule.publicschedule.service.PublicScheduleAdminService;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
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
	class CreatePublicScheduleTest {

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
			mockMvc.perform(post("/admin/calendar/public").header("Authorization", "Bearer " + accessToken)
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
					.detailClassification(DetailClassification.EVENT)
					.eventTag(EventTag.JOB_FORUM)
					.title("취업설명회")
					.content("취업설명회입니다.")
					.status(ScheduleStatus.ACTIVATE)
					.link("https://gg.42seoul.kr")
					.startTime(LocalDateTime.now().plusDays(10))
					.endTime(LocalDateTime.now())
					.build();
				publicScheduleAdminService.createPublicSchedule(requestDto);
			} catch (Exception e) {
				assertThat(e.getMessage()).isEqualTo("종료 시간이 시작 시간보다 빠를 수 없습니다.");
			}
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 제목이 50자가 넘는경우")
		public void createPublicScheduleTitleMax() throws Exception {

			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.title("TEST".repeat(13))
				.content("취업설명회입니다.")
				.status(ScheduleStatus.ACTIVATE)
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10))
				.build();

			mockMvc.perform(post("/admin/calendar/public").header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andDo(print())
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 실패 : 내용이 2000자가 넘는경우")
		public void createPublicScheduleContentMax() throws Exception {

			PublicScheduleAdminCreateReqDto requestDto = PublicScheduleAdminCreateReqDto.builder()
				.detailClassification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.title("취업설명회")
				.content("취업설명회".repeat(401))
				.status(ScheduleStatus.ACTIVATE)
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(10))
				.build();

			mockMvc.perform(post("/admin/calendar/public").header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(requestDto)))
				.andDo(print())
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("Admin PublicSchedule 조회 테스트")
	class GetPublicScheduleAdminTest {

		@Test
		@DisplayName("Admin PublicSchedule 상세 조회 테스트 - 성공")
		void getPublicScheduleAdminDetailTestSuccess() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
						"Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

			PublicScheduleAdminResDto result = objectMapper.readValue(response, PublicScheduleAdminResDto.class);

			// then
			assertThat(result.getId()).isEqualTo(publicSchedule.getId());
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getJobTag()).isEqualTo(publicSchedule.getJobTag());
			assertThat(result.getTechTag()).isEqualTo(publicSchedule.getTechTag());
			assertThat(result.getAuthor()).isEqualTo(publicSchedule.getAuthor());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getSharedCount()).isEqualTo(publicSchedule.getSharedCount());
			assertThat(result.getStatus()).isEqualTo(publicSchedule.getStatus());
		}

		@Test
		@DisplayName("Admin PublicSchedule 상세 조회 테스트 - 실패 : 잘못된 id가 들어왔을 경우")
		void getPublicScheduleAdminDetailTestFailNotCorrectType() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/public/qweksd").header("Authorization",
						"Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

			// then
			log.info("response :{}", response);
		}

		@Test
		@DisplayName("Admin PublicSchedule 상세 조회 테스트 - 실패 : 없는 id가 들어왔을 경우")
		void getPublicScheduleAdminDetailTestFailNotFound() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/public/500123").header("Authorization",
						"Bearer " + accessToken))
				.andDo(print())
				.andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();

			// then
			log.info("response :{}", response);
		}
	}

	@Nested
	@DisplayName("Admin PublicSchedule 수정 테스트")
	class UpdatePublicScheduleAdminTest {

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 성공")
		void updatePublicScheduleAdminTestSuccess() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice")
				.content("Job Notice")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(15))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
							"Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

			PublicScheduleAdminUpdateResDto result = objectMapper.readValue(response,
				PublicScheduleAdminUpdateResDto.class);

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();

			// then
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(
				publicScheduleAdminUpdateReqDto.getClassification().name());
			assertThat(result.getJobTag()).isEqualTo(publicScheduleAdminUpdateReqDto.getJobTag());
			assertThat(result.getTechTag()).isEqualTo(publicScheduleAdminUpdateReqDto.getTechTag());
			assertThat(result.getTitle()).isEqualTo(publicScheduleAdminUpdateReqDto.getTitle());
			assertThat(result.getContent()).isEqualTo(publicScheduleAdminUpdateReqDto.getContent());
			assertThat(result.getLink()).isEqualTo(publicScheduleAdminUpdateReqDto.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicScheduleAdminUpdateReqDto.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicScheduleAdminUpdateReqDto.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 실패 : 종료날짜가 시작날짜보다 빠른경우")
		void updatePublicScheduleAdminTestFailEndBeforeStart() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice")
				.content("Job Notice")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(15))
				.endTime(LocalDateTime.now().plusDays(1))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
							"Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();
			PublicSchedule result = schedules.get(0);
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getContent()).isEqualTo(publicSchedule.getContent());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 실패 : 제목이 50자가 넘는 경우")
		void updatePublicScheduleAdminTestFailTitleMax() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice".repeat(10))
				.content("Job Notice")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(15))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
							"Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();
			PublicSchedule result = schedules.get(0);
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getContent()).isEqualTo(publicSchedule.getContent());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 실패 : 내용이 2000자가 넘는 경우")
		void updatePublicScheduleAdminTestFailContentMax() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice")
				.content("Job Notice".repeat(500))
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(15))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
							"Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();
			PublicSchedule result = schedules.get(0);
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getContent()).isEqualTo(publicSchedule.getContent());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 실패 : 없는 일정을 수정하는 경우")
		void updatePublicScheduleAdminTestFailNotExist() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice")
				.content("Job Notice")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(15))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/100").header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isNotFound())
				.andReturn()
				.getResponse()
				.getContentAsString();

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();
			PublicSchedule result = schedules.get(0);
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getContent()).isEqualTo(publicSchedule.getContent());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 수정 테스트 - 실패 : 잘못된 id가 들어왔을 경우")
		void updatePublicScheduleAdminTestFailBadArgument() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			PublicScheduleAdminUpdateReqDto publicScheduleAdminUpdateReqDto = PublicScheduleAdminUpdateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.jobTag(JobTag.SHORTS_INTERN)
				.techTag(TechTag.BACK_END)
				.title("Job Notice")
				.content("Job Notice")
				.link("https://gg.42seoul.kr")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(15))
				.build();

			// when
			String response = mockMvc.perform(
					put("/admin/calendar/public/asdasdasd").header("Authorization", "Bearer " + accessToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(publicScheduleAdminUpdateReqDto)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			List<PublicSchedule> schedules = publicScheduleAdminRepository.findAll();
			PublicSchedule result = schedules.get(0);
			assertThat(schedules.size()).isEqualTo(1);
			assertThat(result.getClassification()).isEqualTo(publicSchedule.getClassification());
			assertThat(result.getEventTag()).isEqualTo(publicSchedule.getEventTag());
			assertThat(result.getTitle()).isEqualTo(publicSchedule.getTitle());
			assertThat(result.getContent()).isEqualTo(publicSchedule.getContent());
			assertThat(result.getLink()).isEqualTo(publicSchedule.getLink());
			assertThat(result.getStartTime()).isEqualTo(publicSchedule.getStartTime());
			assertThat(result.getEndTime()).isEqualTo(publicSchedule.getEndTime());
		}

		@Test
		@DisplayName("Admin PublicSchedule 삭제 테스트 - 성공")
		void deletePublicScheduleAdminTestSuccess() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			mockMvc.perform(patch("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
						"Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk());

			PublicSchedule result = publicScheduleAdminRepository.findById(publicSchedule.getId()).get();
			assertThat(result.getStatus()).isEqualTo(ScheduleStatus.DELETE);
			System.out.println("PublicSchedule Status : " + result.getStatus());
		}

		@Test
		@DisplayName("Admin PublicSchedule 삭제 테스트 - 실패 : 잘못된 id형식이 들어왔을 경우")
		void deletePublicScheduleAdminTestFailNotBadArgument() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			mockMvc.perform(patch("/admin/calendar/public/qwe1asdv").header("Authorization",
						"Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());

			PublicSchedule result = publicScheduleAdminRepository.findById(publicSchedule.getId()).get();
			assertThat(result.getStatus()).isEqualTo(ScheduleStatus.ACTIVATE);
			System.out.println("PublicSchedule Status : " + result.getStatus());
		}

		@Test
		@DisplayName("Admin PublicSchedule 삭제 테스트 - 실패 : 없는 id가 들어왔을 경우")
		void deletePublicScheduleAdminTestFailNotFound() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();

			// when
			mockMvc.perform(patch("/admin/calendar/public/50123125").header("Authorization",
						"Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isNotFound());

			PublicSchedule result = publicScheduleAdminRepository.findById(publicSchedule.getId()).get();
			assertThat(result.getStatus()).isEqualTo(ScheduleStatus.ACTIVATE);
			System.out.println("PublicSchedule Status : " + result.getStatus());
		}

		@Test
		@DisplayName("Admin PublicSchedule 삭제 테스트 - 실패 : 이미 삭제된 일정을 삭제하는 경우")
		void deletePublicScheduleAdminTestFailAlreadyDelete() throws Exception {
			// given
			PublicSchedule publicSchedule = publicScheduleAdminMockData.createPublicSchedule();
			publicSchedule.delete();
			// when
			mockMvc.perform(patch("/admin/calendar/public/{id}", publicSchedule.getId()).header("Authorization",
						"Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());

			PublicSchedule result = publicScheduleAdminRepository.findById(publicSchedule.getId()).get();
			assertThat(result.getStatus()).isEqualTo(ScheduleStatus.DELETE);
			System.out.println("PublicSchedule Status : " + result.getStatus());
		}
	}
}
