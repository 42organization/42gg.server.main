package gg.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;
import gg.data.user.User;

@Repository
public interface PrivateScheduleRepository extends JpaRepository<PrivateSchedule, Long> {
	List<PrivateSchedule> findByPublicSchedule(PublicSchedule publicSchedule);

	@Query("SELECT pr FROM PrivateSchedule pr "
		+ "JOIN pr.publicSchedule pu "
		+ "WHERE NOT (pu.startTime > :endTime OR pu.endTime < :startTime) "
		+ "AND pr.user = :user")
	List<PrivateSchedule> findOverlappingSchedulesByUser(LocalDateTime startTime, LocalDateTime endTime, User user);
}
