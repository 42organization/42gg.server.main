package gg.calendar.api.admin.schedule.totalschedule.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import gg.calendar.api.admin.schedule.privateschedule.PrivateScheduleAdminMockData;
import gg.calendar.api.admin.schedule.publicschedule.PublicScheduleAdminMockData;
import gg.calendar.api.admin.schedule.totalschedule.controller.request.TotalScheduleAdminSearchReqDto;
import gg.calendar.api.admin.schedule.totalschedule.controller.response.TotalScheduleAdminResDto;
import gg.calendar.api.admin.schedule.totalschedule.controller.response.TotalScheduleAdminSearchListResDto;
import gg.data.calendar.type.DetailClassification;
import gg.data.user.User;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import gg.utils.dto.PageRequestDto;
import gg.utils.dto.PageResponseDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
class TotalScheduleAdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private PublicScheduleAdminMockData publicScheduleAdminMockData;

	@Autowired
	private PrivateScheduleAdminMockData privateScheduleAdminMockData;

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

	@TestInstance(TestInstance.Lifecycle.PER_CLASS)
	@Nested
	@DisplayName("Admin TotalSchedule 조회 테스트")
	class GetTotalScheduleAdminTest {

		private Stream<Arguments> inputParams() {
			return Stream.of(Arguments.of("EVENT", 2, 10), Arguments.of("JOB_NOTICE", 1, 10),
				Arguments.of("PRIVATE_SCHEDULE", 1, 2));
		}

		private Stream<Arguments> inputPageReqDto() {
			return Stream.of(Arguments.of(new PageRequestDto(1, 10)), Arguments.of(new PageRequestDto(2, 10)),
				Arguments.of(new PageRequestDto(2, null)));
		}

		private Stream<Arguments> invalidInput() {
			return Stream.of(Arguments.of(new PageRequestDto(0, 10)),
				Arguments.of(new PageRequestDto(null, null)));
		}

		private Stream<Arguments> inputSearchParam() {
			return Stream.of(
				Arguments.of(new TotalScheduleAdminSearchReqDto("title", "meeting", LocalDate.now(),
					LocalDate.now().plusDays(5))),
				Arguments.of(new TotalScheduleAdminSearchReqDto("content", "urgent", LocalDate.now().plusDays(2),
					LocalDate.now().plusDays(5))),
				Arguments.of(
					new TotalScheduleAdminSearchReqDto("classification", "PRIVATE_SCHEDULE", LocalDate.now(),
						LocalDate.now().plusDays(5))),
				Arguments.of(
					new TotalScheduleAdminSearchReqDto("classification", "EVENT", LocalDate.now(),
						LocalDate.now().plusDays(5))),
				Arguments.of(
					new TotalScheduleAdminSearchReqDto("classification", "JOB_NOTICE", LocalDate.now(),
						LocalDate.now().plusDays(5)))
			);
		}

		private Stream<Arguments> inputSearchParamEndBeforeStart() {
			return Stream.of(
				Arguments.of(new TotalScheduleAdminSearchReqDto("title", "meeting", LocalDate.now().plusDays(5),
					LocalDate.now())),
				Arguments.of(new TotalScheduleAdminSearchReqDto("content", "urgent", LocalDate.now().plusDays(5),
					LocalDate.now().plusDays(2))),
				Arguments.of(
					new TotalScheduleAdminSearchReqDto("classification", "PRIVATE_SCHEDULE",
						LocalDate.now().plusDays(5),
						LocalDate.now()))
			);
		}

		@ParameterizedTest
		@MethodSource("inputParams")
		@DisplayName("Admin TotalSchedule 태그 조회 테스트 - 성공")
		void getTotalAdminClassificationListTestSuccess(String tags, int page, int size) throws Exception {
			// given
			publicScheduleAdminMockData.createPublicScheduleEvent(20);
			publicScheduleAdminMockData.createPublicScheduleJob(10);
			publicScheduleAdminMockData.createPublicSchedulePrivate(5);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("page", String.valueOf(page));
			params.add("size", String.valueOf(size));

			// when
			// multivalue map 을 통해서 값이 넘어옴
			String response = mockMvc.perform(
					get("/admin/calendar/list/{detailClassification}", tags).header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			PageResponseDto<TotalScheduleAdminResDto> pageResponseDto = objectMapper.readValue(response,
				new TypeReference<>() {
				});
			List<TotalScheduleAdminResDto> result = pageResponseDto.getContent();

			if (DetailClassification.valueOf(tags) == DetailClassification.PRIVATE_SCHEDULE) {
				assertThat(result.size()).isEqualTo(2);
			} else {
				assertThat(result.size()).isEqualTo(10);
			}

			for (TotalScheduleAdminResDto dto : result) {
				System.out.println(dto.toString());
			}
		}

		@ParameterizedTest
		@MethodSource("inputPageReqDto")
		@DisplayName("Admin TotalSchedule 태그 조회 테스트 - 실패 : 잘못된 태그가 들어왔을 경우")
		void getTotalAdminClassificationListTestFailNotMatchTag(PageRequestDto pageRequestDto) throws Exception {
			// given
			publicScheduleAdminMockData.createPublicScheduleEvent(20);
			publicScheduleAdminMockData.createPublicScheduleJob(10);
			publicScheduleAdminMockData.createPublicSchedulePrivate(5);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("page", String.valueOf(pageRequestDto.getPage()));
			params.add("size", String.valueOf(pageRequestDto.getSize()));

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/list/{detailClassification}", "qweksd").header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			log.info("response :{}", response);
		}

		@ParameterizedTest
		@MethodSource("inputPageReqDto")
		@DisplayName("Admin TotalSchedule 페이지 조회 테스트 - 성공")
		void getTotalAdminPageNationListTesSuccess(PageRequestDto pageRequestDto) throws Exception {
			// given
			publicScheduleAdminMockData.createPublicScheduleEvent(20);
			publicScheduleAdminMockData.createPublicScheduleJob(10);
			privateScheduleAdminMockData.createPrivateSchedules(5, user);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("page", String.valueOf(pageRequestDto.getPage()));
			params.add("size", String.valueOf(pageRequestDto.getSize()));

			// when
			String response = mockMvc.perform(
					get("/admin/calendar").header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			PageResponseDto<TotalScheduleAdminResDto> pageResponseDto = objectMapper.readValue(response,
				new TypeReference<>() {
				});
			List<TotalScheduleAdminResDto> result = pageResponseDto.getContent();

			for (TotalScheduleAdminResDto dto : result) {
				System.out.println(dto.toString());
			}
		}

		@ParameterizedTest
		@MethodSource("invalidInput")
		@DisplayName("Admin TotalSchedule 페이지 조회 테스트 - 실패 : 페이지 입력값이 잘못된 값일 경우")
		void getTotalAdminPageNationListTestFailInvalidPage(PageRequestDto pageRequestDto) throws Exception {
			// given
			publicScheduleAdminMockData.createPublicScheduleEvent(20);
			publicScheduleAdminMockData.createPublicScheduleJob(10);
			privateScheduleAdminMockData.createPrivateSchedules(5, user);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("page", String.valueOf(pageRequestDto.getPage()));
			params.add("size", String.valueOf(pageRequestDto.getSize()));

			// when
			String response = mockMvc.perform(
					get("/admin/calendar").header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			log.info("response :{}", response);
		}

		@ParameterizedTest
		@MethodSource("inputSearchParam")
		@DisplayName("Admin TotalSchedule 상세 검색 테스트 - 성공")
		void getTotalAdminSearchTestSuccess(TotalScheduleAdminSearchReqDto reqDto) throws Exception {
			// given
			publicScheduleAdminMockData.cratePublicScheduleArgumentsEvent(20, "42GG", "meeting");
			publicScheduleAdminMockData.cratePublicScheduleArgumentsJob(10, "TEST", "urgent");
			privateScheduleAdminMockData.createPrivateSchedules(5, user);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("type", String.valueOf(reqDto.getType()));
			params.add("content", String.valueOf(reqDto.getContent()));
			params.add("startTime", reqDto.getStartTime().toString());
			params.add("endTime", reqDto.getEndTime().toString());

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/search/").header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			log.info("response :{}", response);

			TotalScheduleAdminSearchListResDto result = objectMapper.readValue(response,
				TotalScheduleAdminSearchListResDto.class);
			System.out.println("reqDto.getType() = " + reqDto.getType());
			System.out.println("reqDto.getContent() = " + reqDto.getContent());
			for (TotalScheduleAdminResDto dto : result.getTotalScheduleAdminResDtoList()) {
				System.out.println("asdf : " + dto.toString());
			}
		}

		@ParameterizedTest
		@MethodSource("inputSearchParamEndBeforeStart")
		@DisplayName("Admin TotalSchedule 상세 검색 테스트 - 실패 : 시작 날짜가 끝나는 날짜보다 더 큰 경우")
		void getTotalAdminSearchTestFailEndBeforeStart(TotalScheduleAdminSearchReqDto reqDto) throws Exception {
			// given
			publicScheduleAdminMockData.cratePublicScheduleArgumentsEvent(20, "42GG", "meeting");
			publicScheduleAdminMockData.cratePublicScheduleArgumentsJob(10, "TEST", "urgent");
			privateScheduleAdminMockData.createPrivateSchedules(5, user);

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("type", String.valueOf(reqDto.getType()));
			params.add("content", String.valueOf(reqDto.getContent()));
			params.add("startTime", reqDto.getStartTime().toString());
			params.add("endTime", reqDto.getEndTime().toString());

			// when
			String response = mockMvc.perform(
					get("/admin/calendar/search/").header("Authorization",
						"Bearer " + accessToken).params(params))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andReturn()
				.getResponse()
				.getContentAsString();

			// then
			log.info("response :{}", response);

			TotalScheduleAdminSearchListResDto result = objectMapper.readValue(response,
				TotalScheduleAdminSearchListResDto.class);
			assertNull(result.getTotalScheduleAdminResDtoList());
		}

	}

}
