package gg.admin.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.type.ScheduleStatus;

@Repository
public interface PrivateScheduleAdminRepository extends JpaRepository<PrivateSchedule, Long> {

	List<PrivateSchedule> findByPublicScheduleId(Long publicScheduleId);

	@Query("SELECT ps FROM PrivateSchedule ps " + "JOIN ps.publicSchedule p " + "WHERE ps.alarm = true "
		+ "AND ps.status = :status "
		+ "AND p.endTime BETWEEN :startOfDay AND :endOfDay")
	List<PrivateSchedule> findSchedulesWithAlarmWithStatus(@Param("startOfDay") LocalDateTime startOfDay,
		@Param("endOfDay") LocalDateTime endOfDay,
		@Param("status") ScheduleStatus status);

	@Modifying
	@Transactional
	@Query("UPDATE PrivateSchedule ps SET ps.status = :status WHERE ps.publicSchedule.id IN "
		+ "(SELECT p.id FROM PublicSchedule p WHERE p.status = :publicStatus)")
	void updateRelatedPrivateSchedules(@Param("status") ScheduleStatus status,
		@Param("publicStatus") ScheduleStatus publicStatus);

}
