package gg.repo.party;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import gg.data.party.CommentReport;
import gg.data.party.RoomReport;
import gg.data.user.User;

public interface RoomReportRepository extends JpaRepository<RoomReport, Long> {
	public List<RoomReport> findByRoomId(Long roomId);

	public Optional<RoomReport> findByReporterAndRoomId(User reporter, Long roomId);

	@Query(value = "SELECT rr FROM RoomReport rr "
		+ "JOIN FETCH rr.reporter "
		+ "JOIN FETCH rr.reportee "
		+ "JOIN FETCH rr.room",
		countQuery = "SELECT count(rr) FROM RoomReport rr")
	Page<RoomReport> findAllWithFetchJoin(Pageable pageable);


}
