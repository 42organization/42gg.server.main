package gg.calendar.api.user.utils.controller.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FortyTwoEventResponse {
	private Long id;
	private String name;
	private String description;
	private String location;
	private String kind;

	@JsonProperty("begin_at")
	private LocalDateTime beginAt;

	@JsonProperty("end_at")
	private LocalDateTime endAt;

	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;

	// @JsonProperty("max_people")
	// private Integer maxPeople;
	//
	// @JsonProperty("nbr_subscribers")
	// private Integer nbrSubscribers;

	// @JsonProperty("campus_ids")
	// private List<Integer> campusIds;
	//
	// @JsonProperty("cursus_ids")
	// private List<Integer> cursusIds;

	// @JsonProperty("prohibition_of_cancellation")
	// private String prohibitionOfCancellation;

	// private Waitlist waitlist;
	// private List<Themes> themes;

	// public static class Themes {
	// 	private Long id;
	// 	private String name;
	// 	@JsonProperty("created_at")
	// 	private LocalDateTime createdAt;
	//
	// 	@JsonProperty("updated_at")
	// 	private LocalDateTime updatedAt;
	// }
	//
	// public static class Waitlist {
	// 	private Long id;
	// 	@JsonProperty("created_at")
	// 	private LocalDateTime createdAt;
	//
	// 	@JsonProperty("updated_at")
	// 	private LocalDateTime updatedAt;
	//
	// 	@JsonProperty("waitlist_id")
	// 	private Long waitlistId;
	//
	// 	@JsonProperty("waitlist_type")
	// 	private String waitlistType;
	// }

}
