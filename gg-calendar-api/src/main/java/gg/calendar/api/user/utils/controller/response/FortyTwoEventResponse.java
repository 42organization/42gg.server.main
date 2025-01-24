package gg.calendar.api.user.utils.controller.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FortyTwoEventResponse {
	@JsonProperty("id")
	private Long id;

	@JsonProperty("name")
	private String name;

	@JsonProperty("description")
	private String description;

	@JsonProperty("location")
	private String location;

	@JsonProperty("kind")
	private String kind;

	@JsonProperty("begin_at")
	private LocalDateTime beginAt;

	@JsonProperty("end_at")
	private LocalDateTime endAt;

	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;
}
