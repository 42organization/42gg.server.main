package gg.calendar.api.admin.util.controller.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.EventTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class FortyTwoEventsResponse {
	private Long id;
	private String name;
	private String description;
	private String location;
	private String kind;

	@JsonProperty("max_people")
	private Long maxPeople;

	@JsonProperty("nbr_subscribers")
	private Long nbrSubscribers;

	@JsonProperty("begin_at")
	private LocalDateTime beginAt;

	@JsonProperty("end_at")
	private LocalDateTime endAt;

	@JsonProperty("created_at")
	private LocalDateTime createdAt;

	@JsonProperty("updated_at")
	private LocalDateTime updatedAt;

	public PublicSchedule toPublicSchedule() {
		EventTag eventTag = switch (kind) {
			case "pedago", "rush", "piscine", "partnership", "event", "meet", "hackathon" -> EventTag.OFFICIAL_EVENT;
			case "meet_up" -> EventTag.WENDS_FORUM;
			case "conference" -> EventTag.INSTRUCTION;
			default -> EventTag.ETC;
		};

		String locationInfo = "\n\n장소: " + location;
		String fullDescription = description + locationInfo;
		List<String> descriptionLines = splitDescription(fullDescription);
		String finalDescription = String.join("\n", descriptionLines);
		if (finalDescription.length() > 255) {
			finalDescription = finalDescription.substring(0, 255);
		}
		return new PublicSchedule(eventTag, name, finalDescription, beginAt, endAt, createdAt, updatedAt);
	}

	private List<String> splitDescription(String fullDescription) {
		List<String> result = new ArrayList<>();
		int maxLength = 255;

		while (fullDescription.length() > maxLength) {
			String line = fullDescription.substring(0, maxLength);
			int lastSpaceIndex = line.lastIndexOf(' ');

			if (lastSpaceIndex != -1) {
				line = fullDescription.substring(0, lastSpaceIndex);
				fullDescription = fullDescription.substring(lastSpaceIndex + 1);
			} else {
				fullDescription = fullDescription.substring(maxLength);
			}

			result.add(line);
		}
		if (!fullDescription.isEmpty()) {
			result.add(fullDescription);
		}

		return result;
	}
}
