package gg.admin.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;
import gg.data.calendar.type.ScheduleStatus;

@Repository
public interface PublicScheduleAdminRepository extends JpaRepository<PublicSchedule, Long>,
	JpaSpecificationExecutor<PublicSchedule> {

	List<PublicSchedule> findByAuthor(String author);

	List<PublicSchedule> findAllByClassification(DetailClassification detailClassification);

	List<PublicSchedule> findAll();

	Optional<PublicSchedule> findByTitleAndCreatedAtBetween(String name, LocalDateTime start, LocalDateTime end);

	@Modifying
	@Transactional
	@Query("UPDATE PublicSchedule ps SET ps.status = :status WHERE ps.status = :currentStatus AND ps.endTime < :time")
	void updateExpiredPublicSchedules(@Param("status") ScheduleStatus status,
		@Param("currentStatus") ScheduleStatus currentStatus,
		@Param("time") LocalDateTime time);

}
