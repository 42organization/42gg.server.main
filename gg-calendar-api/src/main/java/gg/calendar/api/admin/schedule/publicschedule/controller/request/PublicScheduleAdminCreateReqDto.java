package gg.calendar.api.admin.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;

import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import gg.calendar.api.user.schedule.publicschedule.controller.request.PublicScheduleCreateReqDto;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.EventTag;
import gg.data.calendar.type.JobTag;
import gg.data.calendar.type.TechTag;
import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.CustomRuntimeException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleAdminCreateReqDto {

	@NotNull
	private String classification;

	private String eventTag;

	private String jobTag;

	private String techTag;

	@NotBlank
	@Size(max = 50, message = "제목은 50자이하로 입력해주세요.")
	private String title;

	@Size(max = 2000, message = "내용은 2000자이하로 입력해주세요.")
	private String content;

	private String link;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startTime;

	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endTime;

	@Builder
	public PublicScheduleAdminCreateReqDto(String detailClassification, String eventTag, String jobTag, String techTag, String title, String content, String link, LocalDateTime startTime, LocalDateTime endTime) {

		if (!EventTag.isValid(eventTag) || !JobTag.isValid(jobTag) || !TechTag.isValid(techTag)) {
			throw new CustomRuntimeException(ErrorCode.BAD_ARGU);
		}

		this.classification = detailClassification;
		this.eventTag = eventTag;
		this.jobTag = jobTag;
		this.techTag = techTag;
		this.title = title;
		this.content = content;
		this.link = link;
		this.startTime = startTime;
		this.endTime = endTime;
	}


	public static PublicSchedule toEntity(PublicScheduleAdminCreateReqDto publicScheduleAdminCreateReqDto){

		if (!DetailClassification.isValid(publicScheduleAdminCreateReqDto.classification) || !EventTag.isValid(publicScheduleAdminCreateReqDto.eventTag)
			|| !JobTag.isValid(publicScheduleAdminCreateReqDto.jobTag) || !TechTag.isValid(publicScheduleAdminCreateReqDto.techTag)) {
			throw new CustomRuntimeException(ErrorCode.BAD_ARGU);
		}

		return PublicSchedule.builder()
			.classification(DetailClassification.getDetailClassificationTag(publicScheduleAdminCreateReqDto.classification))
			.eventTag(EventTag.getEventTag(publicScheduleAdminCreateReqDto.eventTag))
			.jobTag(JobTag.getJobTag(publicScheduleAdminCreateReqDto.jobTag))
			.techTag(TechTag.getTechTag(publicScheduleAdminCreateReqDto.techTag))
			.author("42GG")
			.title(publicScheduleAdminCreateReqDto.title)
			.content(publicScheduleAdminCreateReqDto.content)
			.link(publicScheduleAdminCreateReqDto.link)
			.startTime(publicScheduleAdminCreateReqDto.startTime)
			.endTime(publicScheduleAdminCreateReqDto.endTime)
			.build();
	}
}
