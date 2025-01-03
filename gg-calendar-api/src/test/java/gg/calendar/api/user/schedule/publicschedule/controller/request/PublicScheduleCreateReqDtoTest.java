// package gg.calendar.api.user.schedule.publicschedule.controller.request;
//
// import static org.junit.jupiter.api.Assertions.*;
//
// import java.time.LocalDateTime;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
//
// import gg.auth.UserDto;
// import gg.data.calendar.PublicSchedule;
// import gg.data.calendar.type.DetailClassification;
// import gg.data.calendar.type.EventTag;
// import gg.utils.annotation.UnitTest;
//
// /*이  테스트는 reqDto의 of()메서드가 정상적으로 엔티티로 변환되는 지 검증하는 테스트*/
//
// @UnitTest
// public class PublicScheduleCreateReqDtoTest {
// 	@Test
// 	@DisplayName("PublicScheduleCreateReqDto 생성 성공")
// 	void createPublicScheduleSuccess() {
// 		UserDto user = UserDto.builder().intraId("intraId").build();
//
// 		PublicScheduleCreateReqDto dto = PublicScheduleCreateReqDto.builder()
// 			.classification(DetailClassification.JOB_NOTICE)
// 			.eventTag(EventTag.INSTRUCTION)
// 			.title("Test Schedule")
// 			.author(user.getIntraId())
// 			.content("Test Content")
// 			.link("http://test.com")
// 			.startTime(LocalDateTime.now().plusDays(1))
// 			.endTime(LocalDateTime.now().plusDays(2))
// 			.build();
//
// 		PublicSchedule schedule = dto.toEntity(user.getIntraId(), dto);
//
// 		assertAll(() -> assertNotNull(schedule),
// 			() -> assertEquals(dto.getClassification(), schedule.getClassification()),
// 			() -> assertEquals(dto.getEventTag(), schedule.getEventTag()),
// 			() -> assertEquals(dto.getTitle(), schedule.getTitle()),
// 			() -> assertEquals(user.getIntraId(), schedule.getAuthor()),
// 			() -> assertEquals(dto.getContent(), schedule.getContent()),
// 			() -> assertEquals(dto.getLink(), schedule.getLink()),
// 			() -> assertEquals(dto.getStartTime(), schedule.getStartTime()),
// 			() -> assertEquals(dto.getEndTime(), schedule.getEndTime()));
// 	}
//
// }
