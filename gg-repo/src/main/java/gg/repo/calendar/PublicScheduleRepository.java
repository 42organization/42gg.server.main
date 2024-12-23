package gg.repo.calendar;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;

@Repository
public interface PublicScheduleRepository extends JpaRepository<PublicSchedule, Long> {
	List<PublicSchedule> findByAuthor(String author);
}
