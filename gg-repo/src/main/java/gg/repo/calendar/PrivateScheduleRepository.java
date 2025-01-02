package gg.repo.calendar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PrivateSchedule;
import gg.data.calendar.PublicSchedule;

@Repository
public interface PrivateScheduleRepository extends JpaRepository<PrivateSchedule, Long> {
	List<PrivateSchedule> findByPublicSchedule(PublicSchedule publicSchedule);
}
