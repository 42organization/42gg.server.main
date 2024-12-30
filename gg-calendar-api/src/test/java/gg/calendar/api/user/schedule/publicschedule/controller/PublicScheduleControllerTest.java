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
import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
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
	@DisplayName("공개일정:생성")
	class CreatePublicSchedule {
		@Test
		@DisplayName("공개일정생성성공")
		void createPublicScheduleSuccess() throws Exception {
			// given
			PublicScheduleCreateReqDto publicScheduleDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Test Schedule")
				.content("Test Content")
				.link("http://test.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.build();

			// when
			log.info("After mock data creation: {}", publicScheduleRepository.findByAuthor(user.getIntraId()).size());
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(publicScheduleDto))).andExpect(status().isCreated());
			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo(publicScheduleDto.getTitle());
		}

		@Test
		@DisplayName("공개일정생성실패-작성자가 다를 때")
		void createPublicScheduleFailNotMatchAuthor() throws Exception {
			// given
			PublicScheduleCreateReqDto publicScheduleDto = PublicScheduleCreateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author("another")
				.title("Test Schedule")
				.content("Test Content")
				.link("http://test.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(1))
				.build();
			// when
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(publicScheduleDto)))
				.andExpect(status().isForbidden())
				.andDo(print());
			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).isEmpty();
		}

		@Test
		@DisplayName("공개일정생성실패- 기간이 잘못되었을 때(종료닐짜가 시작날짜보다 빠를때)")
		void createPublicScheduleFailFalutPeriod() throws Exception {
			// given
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
			// when
			mockMvc.perform(post("/calendar/public").header("Authorization", "Bearer " + accssToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(publicScheduleDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest();
				})
				.andDo(print());
			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).isEmpty();
		}
	}

	@Nested
	@DisplayName("공개일정:업데이트")
	class UpdatePublicSchedule {

		@Test
		@DisplayName("공개일정업데이트성공시")
		void updatePublicScheduleSuccess() throws Exception {
			// given
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(),
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author(user.getIntraId())
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());
			publicScheduleRepository.save(publicSchedule);

			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Updated Title")
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when
			mockMvc.perform(
				put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(updateDto))).andExpect(status().isOk());

			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo("Updated Title");
			assertThat(schedules.get(0).getContent()).isEqualTo("Updated Content");
		}

		@Test
		@DisplayName("공개일정업데이트실패-작성자가 다를 때")
		void updatePublicScheduleFailNotMatchAuthor() throws Exception {
			// given
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(),
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author(user.getIntraId())
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());
			publicScheduleRepository.save(publicSchedule);

			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author("another")
				.title("Updated Title")
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when
			mockMvc.perform(
					put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest();
				})
				.andDo(print());

			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo("Original Title");
			assertThat(schedules.get(0).getContent()).isEqualTo("Original Content");
		}

		@Test
		@DisplayName("공개일정업데이트실패-기존일정작성자가다를때")
		void updatePublicScheduleFailExistingAuthorNotMatch() throws Exception {
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity("another",
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author("another")
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());

			publicScheduleRepository.save(publicSchedule);
			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Updated Title")
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();
			mockMvc.perform(
					put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest();
				})
				.andDo(print());

			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(0);
		}

		@Test
		@DisplayName("공개일정업데이트실패-기간이 잘못되었을 때(종료날짜가 시작날짜보다 빠를 때)")
		void updatePublicScheduleFailFaultPeriod() throws Exception {
			// given
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(),
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author(user.getIntraId())
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());
			publicScheduleRepository.save(publicSchedule);

			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Updated Title")
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().minusDays(1))
				.build();

			// when
			mockMvc.perform(
					put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(result -> {
					status().isBadRequest();
				})
				.andDo(print());

			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(1);
			assertThat(schedules.get(0).getTitle()).isEqualTo("Original Title");
			assertThat(schedules.get(0).getContent()).isEqualTo("Original Content");
		}

		@Test
		@DisplayName("공개일정업데이트실패-없는 일정일 때")
		void updatePublicScheduleFailNotExist() throws Exception {
			// given
			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Updated Title")
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when
			mockMvc.perform(put("/calendar/public/9999").header("Authorization", "Bearer " + accssToken)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(updateDto))).andExpect(status().isNotFound()).andDo(print());

			// then
			List<PublicSchedule> schedules = publicScheduleRepository.findByAuthor(user.getIntraId());
			assertThat(schedules).hasSize(0);
		}

		@Test
		@DisplayName("공개일정업데이트실패-제목이 50글자 초과일 때")
		void updatePublicScheduleFailTitleTooLong() throws Exception {
			// given
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(),
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author(user.getIntraId())
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());
			publicScheduleRepository.save(publicSchedule);

			String longTitle = "publicScheduleTest".repeat(20);
			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title(longTitle)
				.content("Updated Content")
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when
			mockMvc.perform(
					put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("제목은 50자이하로 입력해주세요."))
				.andDo(print());

			//then
			PublicSchedule updatedSchedule = publicScheduleRepository.findById(publicSchedule.getId()).get();
			assertThat(updatedSchedule.getTitle()).isEqualTo("Original Title");
		}

		@Test
		@DisplayName("공개일정업데이트실패-내용이 2000글자 초과일 때")
		void updatePublicScheduleFailContentTooLong() throws Exception {
			// given
			PublicSchedule publicSchedule = PublicScheduleCreateReqDto.toEntity(user.getIntraId(),
				PublicScheduleCreateReqDto.builder()
					.classification(DetailClassification.EVENT)
					.author(user.getIntraId())
					.title("Original Title")
					.content("Original Content")
					.link("http://original.com")
					.startTime(LocalDateTime.now())
					.endTime(LocalDateTime.now().plusDays(1))
					.build());
			publicScheduleRepository.save(publicSchedule);

			String longContent = "publicScheduleTest".repeat(200);
			PublicScheduleUpdateReqDto updateDto = PublicScheduleUpdateReqDto.builder()
				.classification(DetailClassification.EVENT)
				.author(user.getIntraId())
				.title("Updated Title")
				.content(longContent)
				.link("http://updated.com")
				.startTime(LocalDateTime.now())
				.endTime(LocalDateTime.now().plusDays(2))
				.build();

			// when
			mockMvc.perform(
					put("/calendar/public/" + publicSchedule.getId()).header("Authorization", "Bearer " + accssToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updateDto)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value("내용은 2000자이하로 입력해주세요."))
				.andDo(print());

			//then
			PublicSchedule updatedSchedule = publicScheduleRepository.findById(publicSchedule.getId()).get();
			assertThat(updatedSchedule.getContent()).isEqualTo("Original Content");
		}
	}
}
