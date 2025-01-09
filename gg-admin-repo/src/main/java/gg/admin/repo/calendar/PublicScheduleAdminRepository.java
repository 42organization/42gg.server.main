package gg.admin.repo.calendar;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PublicSchedule;
import gg.data.calendar.type.DetailClassification;

@Repository
public interface PublicScheduleAdminRepository extends JpaRepository<PublicSchedule, Long>,
	JpaSpecificationExecutor<PublicSchedule> {

	List<PublicSchedule> findByAuthor(String author);

	Page<PublicSchedule> findAllByClassification(DetailClassification detailClassification, Pageable pageable);

	List<PublicSchedule> findAll();
	
	Optional<PublicSchedule> findByTitleAndCreatedAtBetween(String name, LocalDateTime start, LocalDateTime end);
}
