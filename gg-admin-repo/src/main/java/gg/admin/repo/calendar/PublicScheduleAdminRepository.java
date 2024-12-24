package gg.admin.repo.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PublicSchedule;

@Repository
public interface PublicScheduleAdminRepository extends JpaRepository<PublicSchedule, Long> {
}
