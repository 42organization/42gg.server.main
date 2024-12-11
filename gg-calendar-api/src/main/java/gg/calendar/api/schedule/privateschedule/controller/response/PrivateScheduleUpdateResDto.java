package gg.calendar.api.schedule.privateschedule.controller.response;

import java.time.LocalDateTime;
import java.util.Set;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateScheduleUpdateResDto {

	private Long id;

	private DetailClassification detailClassification;

	private Set<Tag> tags;

	private String title;

	private String content;

	private String link;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	private boolean alarm;

	private String color;

	@Builder
	public PrivateScheduleUpdateResDto(Long id, DetailClassification detailClassification, Set<Tag> tags, String title,
		String content, String link, LocalDateTime startTime, LocalDateTime endTime, boolean alarm, String color) {
		this.id = id;
		this.detailClassification = detailClassification;
		this.tags = tags;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
		this.alarm = alarm;
		this.color = color;
	}

	public static PrivateScheduleUpdateResDto of(PrivateSchedule privateSchedule) {

		return PrivateScheduleUpdateResDto.builder()
			.id(privateSchedule.getId())
			.detailClassification(privateSchedule.getPublicSchedule().getClassification())
			.tags(privateSchedule.getPublicSchedule().getTags())
			.title(privateSchedule.getPublicSchedule().getTitle())
			.content(privateSchedule.getPublicSchedule().getContent())
			.link(privateSchedule.getPublicSchedule().getLink())
			.startTime(privateSchedule.getPublicSchedule().getStartTime())
			.endTime(privateSchedule.getPublicSchedule().getEndTime())
			.alarm(privateSchedule.isAlarm())
			.color(privateSchedule.getColor())
			.build();
	}
}
