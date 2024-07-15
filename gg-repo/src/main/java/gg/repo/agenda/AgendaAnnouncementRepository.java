package gg.repo.agenda;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gg.data.agenda.Agenda;
import gg.data.agenda.AgendaAnnouncement;

public interface AgendaAnnouncementRepository extends JpaRepository<AgendaAnnouncement, Long> {

	Optional<AgendaAnnouncement> findFirstByAgendaAndIsShowIsTrueOrderByIdDesc(Agenda agenda);

	@Query("SELECT aa FROM AgendaAnnouncement aa WHERE aa.agenda = :agenda ORDER BY aa.id DESC")
	List<AgendaAnnouncement> findAllByAgenda(Agenda agenda);

	default Optional<AgendaAnnouncement> findLatestByAgenda(Agenda agenda) {
		return findFirstByAgendaAndIsShowIsTrueOrderByIdDesc(agenda);
	}
}
