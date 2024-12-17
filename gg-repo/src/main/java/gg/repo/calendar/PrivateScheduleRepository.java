package gg.repo.calendar;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gg.data.calendar.PrivateSchedule;

@Repository
public interface PrivateScheduleRepository extends JpaRepository<PrivateSchedule, Long> {

	Optional<PrivateSchedule> findByUserIdAndPublicScheduleId(Long userId, Long publicScheduleId);

}
