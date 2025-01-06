package gg.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PublicSchedule;

@Repository
public interface PublicScheduleRepository extends JpaRepository<PublicSchedule, Long> {
	List<PublicSchedule> findByAuthor(String author);

	List<PublicSchedule> findByStartTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
