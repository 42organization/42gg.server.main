package gg.calendar.api.schedule.privateschedule.controller.response;

import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.ScheduleStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PrivateScheduleDetailResDto {

	private Long id;

	private DetailClassification detailClassification;

	private Set<Tag> tags;

	private String author;

	private String title;

	private String content;

	private String link;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;

	private boolean alarm;

	private String color;

	private ScheduleStatus status;


	@Builder
	public PrivateScheduleDetailResDto(Long id, DetailClassification detailClassification, Set<Tag> tags, String author,
		String title, String content, String link, LocalDateTime startTime, LocalDateTime endTime,
		boolean alarm, String color, ScheduleStatus status) {
		this.id = id;
		this.detailClassification = detailClassification;
		this.tags = tags;
		this.author = author;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
		this.alarm = alarm;
		this.color = color;
		this.status = status;
	}

	public static PrivateScheduleDetailResDto of(PrivateSchedule privateSchedule) {
		return PrivateScheduleDetailResDto.builder()
			.id(privateSchedule.getId())
			.detailClassification(privateSchedule.getPublicSchedule().getClassification())
			.tags(privateSchedule.getPublicSchedule().getTags())
			.author(privateSchedule.getPublicSchedule().getAuthor())
			.title(privateSchedule.getPublicSchedule().getTitle())
			.content(privateSchedule.getPublicSchedule().getContent())
			.link(privateSchedule.getPublicSchedule().getLink())
			.startTime(privateSchedule.getPublicSchedule().getStartTime())
			.endTime(privateSchedule.getPublicSchedule().getEndTime())
			.alarm(privateSchedule.isAlarm())
			.color(privateSchedule.getColor())
			.status(privateSchedule.getStatus()).build();
	}

}
