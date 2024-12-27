package gg.calendar.api.user.schedule.publicschedule.controller;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

import gg.calendar.api.user.schedule.publicschedule.PublicScheduleMockData;
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
public class PublicScheduleControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PublicScheduleRepository publicScheduleRepository;

	@Autowired
	private PublicScheduleMockData publicScheduleMockData;

	private User user;
	private String accssToken;
	@Autowired
	private TestDataUtils testDataUtils;

	@Autowired
	EntityManager em;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createNewUser();
		accssToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Nested
	@DisplayName("공개일정생성")
	class CreatePublicSchedule {
		@Test
		@DisplayName("공개일정 생성 성공")
		void createPublicScheduleSuccess() throws Exception {
			// given : reqDto를 생성
			PublicScheduleCreateReqDto publicScheduleDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Test Schedule")
				.content("Test Content")
				.link("http://test.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.build();

			// when : reqDto로 요청
			log.info("After mock data creation: {}", publicScheduleRepository.findByAuthor(user.getIntraId()).size());
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(publicScheduleDto))).andExpect(status().isCreated());
			// then : 생성된 일정이 반환
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo(publicScheduleDto.getTitle());
		}

		@Test
		@DisplayName("공개일정-작성자가 다를 때")
		void createPublicScheduleFail() throws Exception {
			// given : reqDto를 생성
			PublicScheduleCreateReqDto publicScheduleDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author("another")
				.title("Test Schedule")
				.content("Test Content")
				.link("http://test.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.build();
			// when : reqDto로 요청
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(publicScheduleDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest(); // 처음에 errorcode로 잡았는데, status로 잡음
				})
				.andDo(print());
			// then : 예외가 발생
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).isEmpty();
		}

		@Test
		@DisplayName("공개일정- 기간이 잘못되었을 때/ 종료닐짜가 시작날짜보다 빠를때")
		void createPublicScheduleFail2() throws Exception {
			// given : reqDto를 생성
			PublicScheduleCreateReqDto publicScheduleDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.eventTag(EventTag.ETC)
				.author(user.getIntraId())
				.title("Test Schedule")
				.content("Test Content")
				.link("http://test.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().minusDays(1))
				.build();
			// when : reqDto로 요청
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(publicScheduleDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest(); // 처음에 errorcode로 잡았는데, status로 잡음
				})
				.andDo(print());
			// then : 예외가 발생
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).isEmpty();
		}
	}
}
