package gg.admin.repo.calendar;

import org.springframework.data.jpa.repository.JpaRepository;

import gg.data.calendar.PublicSchedule;

public interface CalendarAdminRepository extends JpaRepository<PublicSchedule, Long> {
}
