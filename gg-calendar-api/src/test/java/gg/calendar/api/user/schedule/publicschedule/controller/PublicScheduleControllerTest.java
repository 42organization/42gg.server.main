package gg.calendar.api.user.schedule.publicschedule.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.user.User;
import gg.repo.calendar.PublicScheduleRepository;
import gg.repo.user.UserRepository;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class PublicScheduleControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PublicScheduleRepository publicScheduleRepository;

	private User user;
	private String accssToken;
	@Autowired
	private TestDataUtils testDataUtils;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createNewUser();
		accssToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Nested
	@DisplayName("공개일정생성")
	class CreatePublicSchedule  {
		@Test
		@DisplayName("공개일정 생성 성공")
		void createPublicScheduleSuccess()  throws Exception {
			// given : reqDto를 생성
			PublicScheduleCreateReqDto reqDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.JOB_NOTICE)
				.eventTag(EventTag.INSTRUCTION)
				.title("Test Schedule")
				.author(user.getIntraId())
				.content("Test Content")
				.link("http://test.com")
				.sharedCount(0)
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when : reqDto로 요청
			mockMvc.perform(post("/public")
				.header("Authorization", "Bearer " + accssToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isCreated());

			// then : 생성된 일정이 반환
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo(reqDto.getTitle());
			}
	}
}
