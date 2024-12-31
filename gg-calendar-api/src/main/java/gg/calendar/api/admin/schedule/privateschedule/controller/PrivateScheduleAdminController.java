package gg.calendar.api.admin.schedule.privateschedule.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.calendar.api.admin.schedule.privateschedule.service.PrivateScheduleAdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/calendar/private")
public class PrivateScheduleAdminController {

	private final PrivateScheduleAdminService privateScheduleAdminService;

}
