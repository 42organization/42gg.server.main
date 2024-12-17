package gg.calendar.api.schedule.publicschedule.controller.response;

import java.time.LocalDateTime;
import java.util.Set;

import gg.calendar.api.schedule.publicschedule.controller.request.PublicScheduleUpdateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleUpdateResDto {
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
	public PublicScheduleUpdateResDto(Long id, DetailClassification detailClassification,  Set<Tag> tags, String title,
		String content, String link, LocalDateTime startTime, LocalDateTime endTime, boolean alarm, String color)
	{
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

	public static PublicScheduleUpdateResDto from(PublicSchedule publicSchedule)
	{
		return PublicScheduleUpdateResDto.builder().id(publicSchedule.getId())
			.detailClassification(publicSchedule.getClassification())
			.tags(publicSchedule.getTags())
			.title(publicSchedule.getTitle())
			.content(publicSchedule.getContent())
			.link(publicSchedule.getLink())
			.startTime(publicSchedule.getStartTime())
			.endTime(publicSchedule.getEndTime())
			.alarm(publicSchedule.isAlarm())
			.color(publicSchedule.getColor())
			.build();
	}
}
