package gg.calendar.api.user.utils.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.type.ScheduleStatus;
import gg.repo.calendar.PrivateScheduleRepository;
import gg.repo.calendar.PublicScheduleRepository;
import gg.utils.sns.MessageSender;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleNotiService {
	private final PublicScheduleRepository publicScheduleRepository;
	private final PrivateScheduleRepository privateScheduleRepository;
	private final MessageSender messageSender;
	private static final String SCHEDULE_MESSAGE_D_DAY = "📆일정요정🧚으로부터 알림이 도착했습니다.\n"
		+ "[42ggCalendar]와 오늘의 일정을 확인해보세요!\n";
	private static final String SCHEDULE_MESSAGE_BEFORE_D_DAY = "📅일정요정🧚으로부터 알림이 도착했습니다.\n"
		+ "[42ggCalendar]와 내일의 일정을 확인해보세요!\n";

	@Transactional
	public void sendScheduleNotifications() {
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime startOfDay = LocalDateTime.now().toLocalDate().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);
		LocalDateTime startDday = startOfDay.plusDays(1);
		LocalDateTime endDday = endOfDay.plusDays(1);

		List<PrivateSchedule> alarmSchedule = privateScheduleRepository.findSchedulesWithAlarmForBothDays(startOfDay,
			endOfDay, startDday, endDday, ScheduleStatus.ACTIVATE);
		for (PrivateSchedule schedule : alarmSchedule) {
			String message = schedule.getPublicSchedule().getEndTime()
				.isBefore(currentTime.plusDays(1))
				? SCHEDULE_MESSAGE_D_DAY : SCHEDULE_MESSAGE_BEFORE_D_DAY;
			messageSender.send(schedule.getUser().getIntraId(),
				message + "[" + schedule.getPublicSchedule().getTitle() + "] : "
					+ schedule.getPublicSchedule().getLink() + "\n");
		}
	}
}
