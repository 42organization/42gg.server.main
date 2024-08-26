package gg.agenda.api.user.agendaannouncement.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gg.agenda.api.user.SnsMessageUtil;
import gg.agenda.api.user.agendaannouncement.controller.request.AgendaAnnouncementCreateReqDto;
import gg.data.agenda.Agenda;
import gg.data.agenda.AgendaAnnouncement;
import gg.data.agenda.AgendaTeamProfile;
import gg.repo.agenda.AgendaAnnouncementRepository;
import gg.repo.agenda.AgendaTeamProfileRepository;
import gg.utils.sns.MessageSender;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgendaAnnouncementService {

	private final MessageSender messageSender;
	private final SnsMessageUtil snsMessageUtil;
	private final AgendaTeamProfileRepository agendaTeamProfileRepository;
	private final AgendaAnnouncementRepository agendaAnnouncementRepository;

	@Transactional
	public AgendaAnnouncement addAgendaAnnouncement(AgendaAnnouncementCreateReqDto announceCreateDto, Agenda agenda) {
		AgendaAnnouncement newAnnounce = AgendaAnnouncementCreateReqDto
			.MapStruct.INSTANCE.toEntity(announceCreateDto, agenda);
		return agendaAnnouncementRepository.save(newAnnounce);
	}

	@Transactional(readOnly = true)
	public Page<AgendaAnnouncement> findAnnouncementListByAgenda(Pageable pageable, Agenda agenda) {
		return agendaAnnouncementRepository.findListByAgenda(pageable, agenda);
	}

	@Transactional(readOnly = true)
	public String findLatestAnnounceTitleByAgendaOrDefault(Agenda agenda, String defaultTitle) {
		Optional<AgendaAnnouncement> latestAnnounce = agendaAnnouncementRepository.findLatestByAgenda(agenda);
		if (latestAnnounce.isEmpty()) {
			return defaultTitle;
		}
		return latestAnnounce.get().getTitle();
	}

	public void slackAddAgendaAnnouncement(Agenda agenda, AgendaAnnouncement newAnnounce) {
		List<AgendaTeamProfile> agendaTeamProfiles = agendaTeamProfileRepository.findAllByAgendaAndIsExistTrue(agenda);
		String message = snsMessageUtil.addAgendaAnnouncementMessage(agenda, newAnnounce);
		agendaTeamProfiles.stream()
			.map(atp -> atp.getProfile().getIntraId())
			.forEach(intraId -> messageSender.send(intraId, message));
	}
}
