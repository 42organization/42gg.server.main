package gg.agenda.api.admin.agenda.controller;

import static gg.data.agenda.type.AgendaStatus.*;
import static gg.data.agenda.type.Location.*;
import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import gg.admin.repo.agenda.AgendaAdminRepository;
import gg.data.agenda.type.Location;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import gg.agenda.api.AgendaMockData;
import gg.agenda.api.admin.agenda.controller.request.AgendaAdminUpdateReqDto;
import gg.agenda.api.admin.agenda.controller.response.AgendaAdminResDto;
import gg.data.agenda.Agenda;
import gg.data.agenda.type.AgendaStatus;
import gg.data.user.User;
import gg.utils.TestDataUtils;
import gg.utils.annotation.IntegrationTest;
import gg.utils.dto.PageRequestDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@IntegrationTest
@Transactional
@AutoConfigureMockMvc
public class AgendaAdminControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private TestDataUtils testDataUtils;

	@Autowired
	private AgendaMockData agendaMockData;

	@Autowired
	EntityManager em;

	@Autowired
	AgendaAdminRepository agendaAdminRepository;

	private User user;

	private String accessToken;

	@BeforeEach
	void setUp() {
		user = testDataUtils.createAdminUser();
		accessToken = testDataUtils.getLoginAccessTokenFromUser(user);
	}

	@Nested
	@DisplayName("Admin Agenda 상세 조회")
	class GetAgendaAdmin {

		@ParameterizedTest
		@ValueSource(ints = {1, 2, 3, 4, 5})
		@DisplayName("Admin Agenda 상세 조회 성공")
		void findAgendaByAgendaKeySuccessAdmin(int page) throws Exception {
			// given
			int size = 10;
			List<Agenda> agendas = new ArrayList<>();
			agendas.addAll(agendaMockData.createOfficialAgendaList(5, AgendaStatus.ON_GOING));
			agendas.addAll(agendaMockData.createOfficialAgendaList(5, AgendaStatus.CONFIRM));
			agendas.addAll(agendaMockData.createOfficialAgendaList(5, AgendaStatus.CANCEL));
			agendas.addAll(agendaMockData.createNonOfficialAgendaList(5, AgendaStatus.ON_GOING));
			agendas.addAll(agendaMockData.createNonOfficialAgendaList(5, AgendaStatus.CONFIRM));
			agendas.addAll(agendaMockData.createNonOfficialAgendaList(5, AgendaStatus.CANCEL));
			PageRequestDto pageRequestDto = new PageRequestDto(page, size);
			String request = objectMapper.writeValueAsString(pageRequestDto);

			// when
			String response = mockMvc.perform(get("/admin/agenda/request/list")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			AgendaAdminResDto[] result = objectMapper.readValue(response, AgendaAdminResDto[].class);

			// then
			assertThat(result).hasSize(((page - 1) * size) < agendas.size()
				? Math.min(size, agendas.size() - (page - 1) * size) : 0);
			agendas.sort((a, b) -> b.getId().compareTo(a.getId()));
			for (int i = 0; i < result.length; i++) {
				assertThat(result[i].getAgendaId()).isEqualTo(agendas.get(i + (page - 1) * size).getId());
			}
		}

		@Test
		@DisplayName("Admin Agenda 상세 조회 성공 - 대회가 존재하지 않는 경우")
		void findAgendaByAgendaKeySuccessAdminWithNoContent() throws Exception {
			// given
			int page = 1;
			int size = 10;
			PageRequestDto pageRequestDto = new PageRequestDto(page, size);
			String request = objectMapper.writeValueAsString(pageRequestDto);

			// when
			String response = mockMvc.perform(get("/admin/agenda/request/list")
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
			AgendaAdminResDto[] result = objectMapper.readValue(response, AgendaAdminResDto[].class);

			// then
			assertThat(result).isEmpty();
		}
	}

	@Nested
	@DisplayName("Admin Agenda 수정 맟 삭제")
	class UpdateAgendaAdmin {

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 기본 정보")
		void updateAgendaAdminSuccessWithInformation() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaTitle("updated title").agendaContents("updated content")
				.agendaPoster("updated poster").agendaStatus(CONFIRM)
				.isOfficial(!agenda.getIsOfficial()).isRanking(!agenda.getIsRanking()).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getTitle()).isEqualTo(agendaDto.getAgendaTitle());
			assertThat(updated.get().getContent()).isEqualTo(agendaDto.getAgendaContents());
			assertThat(updated.get().getPosterUri()).isEqualTo(agendaDto.getAgendaPoster());
			assertThat(updated.get().getStatus()).isEqualTo(agendaDto.getAgendaStatus());
			assertThat(updated.get().getIsOfficial()).isEqualTo(agendaDto.getIsOfficial());
			assertThat(updated.get().getIsRanking()).isEqualTo(agendaDto.getIsRanking());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 스케쥴 정보")
		void updateAgendaAdminSuccessWithSchedule() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaDeadLine(agenda.getDeadline().plusDays(1))
				.agendaStartTime(agenda.getStartTime().plusDays(1))
				.agendaEndTime(agenda.getEndTime().plusDays(1)).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getDeadline()).isEqualTo(agendaDto.getAgendaDeadLine());
			assertThat(updated.get().getStartTime()).isEqualTo(agendaDto.getAgendaStartTime());
			assertThat(updated.get().getEndTime()).isEqualTo(agendaDto.getAgendaEndTime());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 서울 대회를 MIX로 변경")
		void updateAgendaAdminSuccessWithLocationSeoulToMix() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);    // SEOUL
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(MIX).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getLocation()).isEqualTo(agendaDto.getAgendaLocation());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 서울 대회를 경산으로 변경")
		void updateAgendaAdminSuccessWithLocationSeoulToGyeongsan() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgenda();
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(GYEONGSAN).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getLocation()).isEqualTo(agendaDto.getAgendaLocation());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 경산 대회를 서울로 변경")
		void updateAgendaAdminSuccessWithLocationGyeongsanToSeoul() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgenda(GYEONGSAN);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(GYEONGSAN).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getLocation()).isEqualTo(agendaDto.getAgendaLocation());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - 경산 대회를 MIX로 변경")
		void updateAgendaAdminSuccessWithLocationGyeongsanToMix() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeamGyeongsan(10);    // SEOUL
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(MIX).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getLocation()).isEqualTo(agendaDto.getAgendaLocation());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - Agenda 팀 제한 정보")
		void updateAgendaAdminSuccessWithAgendaCapacity() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaMinTeam(agenda.getMinTeam() + 1)
				.agendaMaxTeam(agenda.getMaxTeam() + 1)
				.build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getMinTeam()).isEqualTo(agendaDto.getAgendaMinTeam());
			assertThat(updated.get().getMaxTeam()).isEqualTo(agendaDto.getAgendaMaxTeam());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 성공 - Agenda 팀 허용 인원 제한 정보")
		void updateAgendaAdminSuccessWithAgendaTeamCapacity() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaMinPeople(agenda.getMinPeople() + 1)
				.agendaMaxPeople(agenda.getMaxPeople() + 1)
				.build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// when
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isNoContent());
			Optional<Agenda> updated = agendaAdminRepository.findByAgendaKey(agenda.getAgendaKey());

			// then
			assert (updated.isPresent());
			assertThat(updated.get().getMinPeople()).isEqualTo(agendaDto.getAgendaMinPeople());
			assertThat(updated.get().getMaxPeople()).isEqualTo(agendaDto.getAgendaMaxPeople());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 대회가 존재하지 않는 경우")
		void updateAgendaAdminFailedWithNoAgenda() throws Exception {
			// given
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder().build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", UUID.randomUUID().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 서울 대회를 경산으로 변경할 수 없는 경우")
		void updateAgendaAdminFailedWithLocationSeoulToGyeongSan() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeam(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(GYEONGSAN).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 경산 대회를 서울 대회로 변경할 수 없는 경우")
		void updateAgendaAdminFailedWithLocationGyeongSanToSeoul() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeamGyeongsan(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(SEOUL).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@ParameterizedTest
		@EnumSource(value = Location.class, names = {"SEOUL", "GYEONGSAN"})
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 혼합 대회를 다른 지역 대회로 변경할 수 없는 경우")
		void updateAgendaAdminFailedWithLocationMixToSeoul() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeamMix(10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaLocation(SEOUL).build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 이미 maxTeam 이상의 팀이 존재하는 경우")
		void updateAgendaAdminFailedWithMaxTeam() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeamAndAgendaCapacity(10, 2, 10);
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaMinTeam(agenda.getMinTeam())	// TODO : Null인 경우 허용
				.agendaMaxTeam(agenda.getMaxTeam() - 5)
				.build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 이미 확정된 대회에 minTeam 이하의 팀이 참여한 경우")
		void updateAgendaAdminFailedWithMinTeam() throws Exception {
			// given
			Agenda agenda = agendaMockData.createAgendaWithTeamAndAgendaCapacity(5, 5, 10);
			agenda.confirm(LocalDateTime.now());	// TODO : confirm 하고 em.flush();
			AgendaAdminUpdateReqDto agendaDto = AgendaAdminUpdateReqDto.builder()
				.agendaMinTeam(agenda.getMinTeam() + 2)
				.build();
			String request = objectMapper.writeValueAsString(agendaDto);

			// expected
			mockMvc.perform(patch("/admin/agenda/request")
					.param("agenda_key", agenda.getAgendaKey().toString())
					.header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON)
					.content(request))
				.andExpect(status().isBadRequest());
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 이미 팀에 maxPeople 이상의 인원이 참여한 경우")
		void updateAgendaAdminFailedWithMaxPeople() {
			// given
			// when
			// then
		}

		@Test
		@DisplayName("Admin Agenda 수정 맟 삭제 실패 - 이미 확정된 팀에 minPeople 이하의 인원이 참여한 경우")
		void updateAgendaAdminFailedWithMinPeople() {
			// given
			// when
			// then
		}
	}
}
