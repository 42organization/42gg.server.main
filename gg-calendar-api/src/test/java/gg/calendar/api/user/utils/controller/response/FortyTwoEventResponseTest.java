package gg.calendar.api.user.utils.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import gg.utils.annotation.UnitTest;
import lombok.Getter;

@Getter
@UnitTest
class FortyTwoEventResponseTest {

	@Test
	@DisplayName("FortyTwoEventResDto 생성자 테스트")
	void toSuccess() throws JsonProcessingException {
		//Given

		ObjectMapper objectMapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		String jsonResponse = "{"
			+ "\"id\": 1,"
			+ "\"name\": \"Exam06\","
			+ "\"description\": \"ft_irc\","
			+ "\"location\": \"location\","
			+ "\"kind\": \"event\","
			+ "\"begin_at\": \"2025-01-19T10:00:00\","
			+ "\"end_at\": \"2025-01-30T12:00:00\","
			+ "\"created_at\": \"2025-01-17T10:00:00\","
			+ "\"updated_at\": \"2025-01-18T12:00:00\""
			+ "}";
		//When
		FortyTwoEventResponse response = objectMapper.readValue(jsonResponse, FortyTwoEventResponse.class);

		//Then
		assertThat(response.getId()).isEqualTo(1L);
		assertThat(response.getName()).isEqualTo("Exam06");
		assertThat(response.getDescription()).isEqualTo("ft_irc");
		assertThat(response.getLocation()).isEqualTo("location");
		assertThat(response.getKind()).isEqualTo("event");
		assertThat(response.getBeginAt()).isEqualTo(LocalDateTime.parse("2025-01-19T10:00:00"));
		assertThat(response.getEndAt()).isEqualTo(LocalDateTime.parse("2025-01-30T12:00:00"));
		assertThat(response.getCreatedAt()).isEqualTo(LocalDateTime.parse("2025-01-17T10:00:00"));
		assertThat(response.getUpdatedAt()).isEqualTo(LocalDateTime.parse("2025-01-18T12:00:00"));
	}

}
