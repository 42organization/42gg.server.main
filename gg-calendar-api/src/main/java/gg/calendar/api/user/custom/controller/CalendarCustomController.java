package gg.calendar.api.user.custom.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.user.custom.service.CalendarCustomService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/custom")
public class CalendarCustomController {
	private final CalendarCustomService calendarCustomService;
}
