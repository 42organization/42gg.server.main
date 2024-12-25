package gg.calendar.api.admin.schedule.publicschedule.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import gg.calendar.api.admin.PublicScheduleMockData;
import gg.calendar.api.admin.schedule.publicschedule.controller.request.PublicScheduleAdminCreateReqDto;
import gg.data.calendar.PublicSchedule;
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
	private PublicScheduleMockData publicScheduleMockData;

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
		@Test
		@DisplayName("Admin PublicSchedule 등록 테스트 - 성공")
		void createPublicScheduleTestSuccess() throws Exception {
			// given
			PublicScheduleAdminCreateReqDto publicScheduleAdminReqDto = publicScheduleMockData.createPublicScheduleAdminCreateReqDto();

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
	}
}
