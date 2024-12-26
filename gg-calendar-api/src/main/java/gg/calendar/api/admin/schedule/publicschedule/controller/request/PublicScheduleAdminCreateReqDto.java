package gg.calendar.api.admin.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.ScheduleStatus;
import gg.data.calendar.type.TechTag;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleAdminCreateReqDto {

	@NotNull
	private DetailClassification classification;

	private EventTag eventTag;

	private JobTag jobTag;

	private TechTag techTag;

	@NotBlank
	@Size(max = 50, message = "제목은 50자이하로 입력해주세요.")
	private String title;

	@Size(max = 2000, message = "내용은 2000자이하로 입력해주세요.")
	private String content;

	private String link;

	private ScheduleStatus status;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;

	@Builder
	public PublicScheduleAdminCreateReqDto(DetailClassification detailClassification, EventTag eventTag, JobTag jobTag, TechTag techTag, String title, String content, String link, ScheduleStatus status,LocalDateTime startTime, LocalDateTime endTime) {

		this.classification = detailClassification;
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.title = title;
		this.content = content;
		this.link = link;
		this.status = status;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public static PublicSchedule toEntity(PublicScheduleAdminCreateReqDto publicScheduleAdminCreateReqDto){

		return PublicSchedule.builder()
			.classification(publicScheduleAdminCreateReqDto.classification)
			.eventTag(publicScheduleAdminCreateReqDto.eventTag)
			.jobTag(publicScheduleAdminCreateReqDto.jobTag)
			.techTag(publicScheduleAdminCreateReqDto.techTag)
			.author("42GG")
			.title(publicScheduleAdminCreateReqDto.title)
			.content(publicScheduleAdminCreateReqDto.content)
			.link(publicScheduleAdminCreateReqDto.link)
			.status(publicScheduleAdminCreateReqDto.status)
			.startTime(publicScheduleAdminCreateReqDto.startTime)
			.endTime(publicScheduleAdminCreateReqDto.endTime)
			.build();
	}
}
