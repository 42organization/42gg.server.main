package gg.repo.agenda;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import gg.data.agenda.Agenda;
import gg.data.agenda.AgendaTeam;
import gg.data.agenda.type.AgendaTeamStatus;

public interface AgendaTeamRepository extends JpaRepository<AgendaTeam, Long> {
	@Query("SELECT a FROM AgendaTeam a WHERE a.agenda = :agenda AND a.name = :teamName AND"
		+ " (a.status = :status1 OR a.status = :status2)")
	Optional<AgendaTeam> findByAgendaAndTeamNameAndStatus(Agenda agenda, String teamName, AgendaTeamStatus status1,
		AgendaTeamStatus status2);

	@Query("SELECT a FROM AgendaTeam a WHERE a.agenda = :agenda AND a.teamKey = :teamKey AND"
		+ " (a.status = :status1 OR a.status = :status2)")
	Optional<AgendaTeam> findByAgendaAndTeamKeyAndStatus(Agenda agenda, UUID teamKey, AgendaTeamStatus status,
		AgendaTeamStatus status2);

	@Query("SELECT a FROM AgendaTeam a WHERE a.teamKey = :teamKey")
	Optional<AgendaTeam> findByTeamKey(UUID teamKey);
}
