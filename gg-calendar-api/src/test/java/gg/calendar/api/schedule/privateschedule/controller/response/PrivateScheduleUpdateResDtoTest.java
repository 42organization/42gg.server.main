package gg.calendar.api.schedule.privateschedule.controller.response;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import gg.auth.UserDto;
import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.calendar.Tag;
import gg.data.calendar.type.DetailClassification;
import gg.data.user.User;
import gg.utils.annotation.UnitTest;

@UnitTest
public class PrivateScheduleUpdateResDtoTest {

	@Test
	@DisplayName("PrivateScheduleUpdateResDto 생성 성공")
	void createPrivateScheduleUpdateResDtoSuccess() {
		// given
		// user 생성
		User user = User.builder().intraId("intraId").build();

		// 개인일정 생성
		PublicSchedule publicSchedule = PublicSchedule.builder()
			.author(user.getIntraId())
			.classification(DetailClassification.NONE)
			.tags(new HashSet<>())
			.title("title")
			.content("content")
			.link("link")
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.of(2025, 1, 31, 0, 0))
			.build();

		// 태그 생성
		Tag tag = new Tag(publicSchedule, "취업공고");
		Tag tag2 = new Tag(publicSchedule, "신입");
		Tag tag3 = new Tag(publicSchedule, "FE");

		//태그 추가
		publicSchedule.addTag(tag);
		publicSchedule.addTag(tag2);
		publicSchedule.addTag(tag3);

		// 개인일정 생성
		PrivateSchedule privateSchedule = PrivateSchedule.builder()
			.user(user)
			.publicSchedule(publicSchedule)
			.color("#FFFFFF")
			.build();

		// PrivateScheduleUpdateResDto 생성
		PrivateScheduleUpdateResDto dto = PrivateScheduleUpdateResDto.builder()
			.id(1L)
			.detailClassification(DetailClassification.NONE)
			.tags(new HashSet<>(Arrays.asList(tag, tag2, tag3)))
			.title("title")
			.content("content")
			.link("link")
			.alarm(false)
			.color("#FFFFFF")
			.startTime(LocalDateTime.now())
			.endTime(LocalDateTime.of(2025, 1, 31, 0, 0))
			.build();

		// when
		// PrivateScheduleUpdateResDto Of 테스트 생성
		PrivateScheduleUpdateResDto privateScheduleUpdateResDto = PrivateScheduleUpdateResDto.of(privateSchedule);

		// then
		// assertThat(privateScheduleUpdateResDto).isEqualTo(dto);
		assertThat(privateScheduleUpdateResDto.getDetailClassification()).isEqualTo(dto.getDetailClassification());
		assertThat(privateScheduleUpdateResDto.getTags()).isEqualTo(dto.getTags());
		assertThat(privateScheduleUpdateResDto.getTitle()).isEqualTo(dto.getTitle());
		assertThat(privateScheduleUpdateResDto.getContent()).isEqualTo(dto.getContent());
		assertThat(privateScheduleUpdateResDto.getLink()).isEqualTo(dto.getLink());
		assertThat(privateScheduleUpdateResDto.getColor()).isEqualTo(dto.getColor());
		assertThat(privateScheduleUpdateResDto.getEndTime()).isEqualTo(dto.getEndTime());
	}
}
