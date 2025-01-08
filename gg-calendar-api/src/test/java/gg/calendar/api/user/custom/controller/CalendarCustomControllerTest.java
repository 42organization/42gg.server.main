package gg.calendar.api.user.custom.controller;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

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

import gg.calendar.api.user.custom.CalendarCustomMockData;
import gg.calendar.api.user.custom.controller.request.CalendarCustomCreateReqDto;
import gg.calendar.api.user.custom.controller.request.CalendarCustomUpdateReqDto;
import gg.data.calendar.ScheduleGroup;
import gg.data.user.User;
import gg.repo.calendar.ScheduleGroupRepository;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@AutoConfigureMockMvc
@Transactional
public class CalendarCustomControllerTest {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ScheduleGroupRepository scheduleGroupRepository;

	@Autowired
	private CalendarCustomMockData calendarCustomMockData;

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
	@DisplayName("ScheduleGroup 생성하기")
	class CreatePrivateSchedule {
		@Test
		@DisplayName("성공 201")
		void success() throws Exception {
			//given
			CalendarCustomCreateReqDto reqDto = CalendarCustomCreateReqDto.builder()
				.title("공부")
				.backgroundColor("#FFFFFF")
				.build();
			//when
			mockMvc.perform(post("/calendar/custom")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isCreated());
			//then
			List<ScheduleGroup> scheduleGroups = scheduleGroupRepository.findAll();
			assertThat(scheduleGroups.size()).isEqualTo(1);
			ScheduleGroup scheduleGroup = scheduleGroups.get(0);
			Assertions.assertThat(scheduleGroup.getTitle()).isEqualTo(reqDto.getTitle());
			Assertions.assertThat(scheduleGroup.getBackgroundColor()).isEqualTo(reqDto.getBackgroundColor());
		}

		@Test
		@DisplayName("잘못된 색상코드(hex code)가 들어온 경우 400")
		void invalidHexColorCode() throws Exception {
			//given
			CalendarCustomCreateReqDto reqDto = CalendarCustomCreateReqDto.builder()
				.title("공부")
				.backgroundColor("잘못된 색상 코드")
				.build();
			//when&then
			mockMvc.perform(post("/calendar/custom")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isBadRequest());
		}
	}

	@Nested
	@DisplayName("ScheduleGroup 수정하기")
	class UpdatePrivateSchedule {
		@Test
		@DisplayName("성공 200")
		void success() throws Exception {
			//given
			ScheduleGroup scheduleGroup = calendarCustomMockData.createScheduleGroup(user);
			CalendarCustomUpdateReqDto reqDto = CalendarCustomUpdateReqDto.builder()
				.title("공부")
				.backgroundColor("#AAAAAA")
				.build();
			//when
			mockMvc.perform(put("/calendar/custom/" + scheduleGroup.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isOk());
			//then
			ScheduleGroup updated = scheduleGroupRepository.findById(scheduleGroup.getId()).orElseThrow();
			Assertions.assertThat(updated.getTitle()).isEqualTo(reqDto.getTitle());
			Assertions.assertThat(updated.getBackgroundColor()).isEqualTo(reqDto.getBackgroundColor());
		}

		@Test
		@DisplayName("잘못된 색상코드(hex code)가 들어온 경우 400")
		void invalidHexColorCode() throws Exception {
			//given
			ScheduleGroup scheduleGroup = calendarCustomMockData.createScheduleGroup(user);
			CalendarCustomUpdateReqDto reqDto = CalendarCustomUpdateReqDto.builder()
				.title("공부")
				.backgroundColor("잘못된 색상 코드")
				.build();
			//when&then
			mockMvc.perform(put("/calendar/custom/" + scheduleGroup.getId())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("스케줄 그룹이 없는 경우 404")
		void notFoundGroup() throws Exception {
			//given
			ScheduleGroup scheduleGroup = calendarCustomMockData.createScheduleGroup(user);
			CalendarCustomUpdateReqDto reqDto = CalendarCustomUpdateReqDto.builder()
				.title("공부")
				.backgroundColor("#AAAAAA")
				.build();
			//when&then
			mockMvc.perform(put("/calendar/custom/" + scheduleGroup.getId() + 12341234)
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(reqDto)))
				.andExpect(status().isNotFound());
		}
	}
}
