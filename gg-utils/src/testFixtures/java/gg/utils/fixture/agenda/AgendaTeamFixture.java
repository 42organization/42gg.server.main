package gg.utils.fixture.agenda;

import static gg.data.agenda.type.AgendaTeamStatus.*;
import static gg.data.agenda.type.Location.*;
import static java.util.UUID.*;

import gg.data.agenda.type.AgendaStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import gg.data.agenda.Agenda;
import gg.data.agenda.AgendaTeam;
import gg.data.agenda.type.AgendaTeamStatus;
import gg.data.agenda.type.Location;
import gg.data.user.User;
import gg.repo.agenda.AgendaTeamRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AgendaTeamFixture {
	private final AgendaTeamRepository agendaTeamRepository;

	public AgendaTeam createAgendaTeam(Agenda agenda) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(UUID.randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId("leaderIntraId")
			.status(OPEN)
			.location(MIX)
			.mateCount(3)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public AgendaTeam createAgendaTeam(Agenda agenda, Location location) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(UUID.randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId("leaderIntraId")
			.status(OPEN)
			.location(location)
			.mateCount(1)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public AgendaTeam createAgendaTeam(int mateCount, Agenda agenda, User seoulUser, Location location) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(UUID.randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId(seoulUser.getIntraId())
			.status(OPEN)
			.location(location)
			.mateCount(mateCount)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public AgendaTeam createAgendaTeam(Agenda agenda, Location location, AgendaTeamStatus agendaTeamStatus) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(UUID.randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId("leaderIntraId")
			.status(agendaTeamStatus)
			.location(location)
			.mateCount(3)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public AgendaTeam createAgendaTeam(Agenda agenda, User user, Location location) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId(user.getIntraId())
			.status(OPEN)
			.location(location)
			.mateCount(3)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public AgendaTeam createAgendaTeam(Agenda agenda, User user, Location location, AgendaTeamStatus status) {
		AgendaTeam agendaTeam = AgendaTeam.builder()
			.agenda(agenda)
			.teamKey(randomUUID())
			.name("name")
			.content("content")
			.leaderIntraId(user.getIntraId())
			.status(status)
			.location(location)
			.mateCount(3)
			.awardPriority(1)
			.isPrivate(false)
			.build();
		return agendaTeamRepository.save(agendaTeam);
	}

	public List<AgendaTeam> createAgendaTeamList(Agenda agenda, AgendaTeamStatus status, int size) {
		List<AgendaTeam> teams = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			AgendaTeam agendaTeam = AgendaTeam.builder()
				.agenda(agenda)
				.teamKey(randomUUID())
				.name("name")
				.content("content")
				.leaderIntraId("intraId" + i)
				.status(status)
				.location(SEOUL)
				.mateCount(3)
				.awardPriority(1)
				.isPrivate(false)
				.build();
			teams.add(agendaTeam);
		}
		return agendaTeamRepository.saveAll(teams);
	}
}
