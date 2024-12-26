package gg.admin.repo.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PrivateSchedule;

@Repository
public interface PrivateScheduleAdminRepository extends JpaRepository<PrivateSchedule, Long> {
}
