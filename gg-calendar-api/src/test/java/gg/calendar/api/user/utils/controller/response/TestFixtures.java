package gg.calendar.api.user.utils.controller.response;

import java.time.LocalDateTime;

import org.springframework.test.util.ReflectionTestUtils;

public class TestFixtures {
	public static FortyTwoEventResponse createTestEvent(String name, String kind, LocalDateTime beginAt) {
		FortyTwoEventResponse response = new FortyTwoEventResponse() {
		};  // 익명 클래스 선언 끝

		ReflectionTestUtils.setField(response, "name", name);
		ReflectionTestUtils.setField(response, "kind", kind);
		ReflectionTestUtils.setField(response, "beginAt", beginAt);
		ReflectionTestUtils.setField(response, "endAt", beginAt.plusHours(2));
		ReflectionTestUtils.setField(response, "description", "Sample description");

		return response;
	}
}
