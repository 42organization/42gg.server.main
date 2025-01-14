package gg.calendar.api.user.fortytwoevent.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.calendar.api.user.fortytwoevent.controller.response.FortyTwoEventResponse;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.repo.calendar.PublicScheduleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FortyTwoEventService {
	private final FortyTwoEventApiClient fortyTwoEventApiClient;
	private final PublicScheduleRepository publicScheduleRepository;

	public void checkEvent() {
		List<FortyTwoEventResponse> events = fortyTwoEventApiClient.getEvents();
		List<FortyTwoEventResponse> newEvents = filterEvents(events);
		saveEventsToPublicSchedule(newEvents);
	}

	private List<FortyTwoEventResponse> filterEvents(List<FortyTwoEventResponse> events) {
		return events.stream()
			.filter(event -> !isEventExists(event.getName(), event.getBeginAt()))
			.collect(Collectors.toList());
	}

	private void saveEventsToPublicSchedule(List<FortyTwoEventResponse> events) {
		events.forEach(this::convertAndSaveEvent);
	}

	private void convertAndSaveEvent(FortyTwoEventResponse event) {
		String description = event.getDescription();
		if (description != null && description.length() > 255) {
			description = description.substring(0, 255);
		}
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.classification(DetailClassification.EVENT)
			.eventTag(determineEventTag(event))
			.author("42Seoul")
			.title(event.getName())
			.content(description)
			.link("https://profile.intra.42.fr/events")
			.status(ScheduleStatus.ACTIVATE)
			.startTime(event.getBeginAt())
			.endTime(event.getEndAt())
			.build();
		publicScheduleRepository.save(publicSchedule);
	}

	private boolean isEventExists(String title, LocalDateTime beginAt) {
		return publicScheduleRepository.existsByTitleAndStartTime(title, beginAt);
	}

	private EventTag determineEventTag(FortyTwoEventResponse eventResponse) {
		return switch (eventResponse.getKind()) {
			case "pedago", "rush", "piscine", "partnership", "event", "meet", "hackathon" -> EventTag.OFFICIAL_EVENT;
			case "meet_up" -> EventTag.WENDS_FORUM;
			case "conference" -> EventTag.INSTRUCTION;
			default -> EventTag.ETC;
		};
	}
}


