package gg.calendar.api.user.utils.service;

import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import gg.calendar.api.user.utils.controller.response.FortyTwoEventResponse;
import gg.calendar.api.user.utils.controller.response.TestFixtures;
import gg.data.calendar.PublicSchedule;
import gg.repo.calendar.PublicScheduleRepository;

@ExtendWith(MockitoExtension.class)
class FortyTwoEventServiceTest {

	@Mock
	private FortyTwoEventApiClient fortyTwoEventClient;

	@Mock
	private PublicScheduleRepository publicScheduleRepository;

	@InjectMocks
	private FortyTwoEventService fortyTwoEventService;

	private FortyTwoEventResponse sample;
	private LocalDateTime now;

	@BeforeEach
	void setUp() {
		now = LocalDateTime.now();
		sample = createSampleEvent("Test", "event", now);
	}

	@Test
	@DisplayName("새로운 이벤트 저장 성공")
	void checkAndSaveEvent() {
		//given
		when(fortyTwoEventClient.getEvents()).thenReturn(Arrays.asList(sample));
		when(publicScheduleRepository.existsByTitleAndStartTime(any(), any())).thenReturn(false);

		//when
		fortyTwoEventService.checkEvent();

		//then
		verify(publicScheduleRepository, times(1)).save(any(PublicSchedule.class));
	}

	@Test
	@DisplayName("이미 있는 이벤트 저장하지 않음")
	void checkAndNotSaveEvent() {
		//given
		when(fortyTwoEventClient.getEvents()).thenReturn(Arrays.asList(sample));
		when(publicScheduleRepository.existsByTitleAndStartTime(any(), any())).thenReturn(true);

		//when
		fortyTwoEventService.checkEvent();

		//then
		verify(publicScheduleRepository, times(0)).save(any(PublicSchedule.class));
	}

	private FortyTwoEventResponse createSampleEvent(String name, String kind, LocalDateTime begin) {
		FortyTwoEventResponse testEvent = TestFixtures.createTestEvent(name, kind, begin);
		return testEvent;
	}
}
