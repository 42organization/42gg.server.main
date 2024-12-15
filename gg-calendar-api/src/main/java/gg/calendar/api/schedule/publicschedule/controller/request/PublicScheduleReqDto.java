package gg.calendar.api.schedule.publicschedule.controller.request;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PublicScheduleReqDto {

}
