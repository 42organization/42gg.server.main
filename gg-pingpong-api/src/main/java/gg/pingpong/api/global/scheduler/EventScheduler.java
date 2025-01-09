package gg.pingpong.api.global.scheduler;

import java.util.List;

import org.springframework.stereotype.Component;

import gg.calendar.api.admin.util.GetFortyTwoEvents;
import gg.calendar.api.admin.util.controller.response.FortyTwoEventsResponse;
import gg.calendar.api.admin.util.service.FortyTwoEventsRegisterService;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class EventScheduler extends AbstractScheduler {

	private final GetFortyTwoEvents events;

	private final FortyTwoEventsRegisterService fortyTwoEventsRegisterService;

	public EventScheduler(GetFortyTwoEvents events, FortyTwoEventsRegisterService fortyTwoEventsRegisterService) {
		this.events = events;
		this.fortyTwoEventsRegisterService = fortyTwoEventsRegisterService;
		this.setCron("0 0/1 * * * *");
	}

	@Override
	public Runnable runnable() {
		return () -> {
			log.info("Set 42 Event List ");
			List<FortyTwoEventsResponse> eventsLists = events.getEvents();
			fortyTwoEventsRegisterService.registerFortyTwoEvents(eventsLists);

		};
	}
}
