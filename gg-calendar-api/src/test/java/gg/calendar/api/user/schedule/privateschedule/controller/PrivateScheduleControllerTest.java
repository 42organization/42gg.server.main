package gg.calendar.api.user.schedule.privateschedule.controller;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import gg.calendar.api.user.schedule.privateschedule.PrivateScheduleMockData;
import gg.calendar.api.user.schedule.privateschedule.controller.request.PrivateScheduleCreateReqDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.ScheduleGroup;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.user.User;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;

@Transactional
@IntegrationTest
@AutoConfigureMockMvc
public class PrivateScheduleControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private PrivateScheduleRepository privateScheduleRepository;

	@Autowired
	private PrivateScheduleMockData privateScheduleMockData;

	@Autowired
	private TestDataUtils testDataUtils;

	private User user;
	private String accessToken;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createNewUser();
		accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Nested
	@DisplayName("PrivateSchedule 생성하기")
	class CreatePrivateSchedule {
		@Test
		void success() throws Exception {
			//given
			ScheduleGroup scheduleGroup = privateScheduleMockData.createScheduleGroup(user);
			PrivateScheduleCreateReqDto reqDto = PrivateScheduleCreateReqDto.builder()
				.classification(DetailClassification.PRIVATE_SCHEDULE)
				.eventTag(EventTag.ETC)
				.jobTag(null)
				.techTag(null)
				.title("title")
				.link("")
				.content("")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.alarm(true)
				.groupId(1L)
				.status(ScheduleStatus.ACTIVATE)
				.build();
			//when
			mockMvc.perform(post("/calendar/private")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isCreated());
			//then
			PrivateSchedule privateSchedule = privateScheduleRepository.findById(1L).orElseThrow();
			Assertions.assertThat(privateSchedule.getGroupId()).isEqualTo(scheduleGroup.getId());
			Assertions.assertThat(privateSchedule.isAlarm()).isEqualTo(reqDto.isAlarm());
		}

		@Test
		void noScheduleGroup() throws Exception {
			//given
			PrivateScheduleCreateReqDto reqDto = PrivateScheduleCreateReqDto.builder()
				.classification(DetailClassification.PRIVATE_SCHEDULE)
				.eventTag(EventTag.ETC)
				.jobTag(null)
				.techTag(null)
				.title("title")
				.link("")
				.content("")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.alarm(true)
				.groupId(2L)
				.status(ScheduleStatus.ACTIVATE)
				.build();
			//when&then
			mockMvc.perform(post("/calendar/private")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isNotFound());
		}

		@Test
		void endDateBeforeStartDate() throws Exception {
			//given
			PrivateScheduleCreateReqDto reqDto = PrivateScheduleCreateReqDto.builder()
				.classification(DetailClassification.PRIVATE_SCHEDULE)
				.eventTag(EventTag.ETC)
				.jobTag(null)
				.techTag(null)
				.title("title")
				.link("")
				.content("")
				.startTime(LocalDateTime.now().plusDays(1))
				.endTime(LocalDateTime.now())
				.alarm(true)
				.groupId(2L)
				.status(ScheduleStatus.ACTIVATE)
				.build();
			//when&then
			mockMvc.perform(post("/calendar/private")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isBadRequest());
		}
	}
}
