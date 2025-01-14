package gg.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;

@Repository
public interface PublicScheduleRepository extends JpaRepository<PublicSchedule, Long> {
	List<PublicSchedule> findByAuthor(String author);

	List<PublicSchedule> findByEndTimeGreaterThanEqualAndStartTimeLessThanEqual(LocalDateTime startTime,
		LocalDateTime endTime);

	List<PublicSchedule> findByEndTimeGreaterThanEqualAndStartTimeLessThanEqualAndClassification(
		LocalDateTime startTime, LocalDateTime endTime, DetailClassification classification);

	boolean existsByTitleAndStartTime(String title, LocalDateTime beginAt);
}
