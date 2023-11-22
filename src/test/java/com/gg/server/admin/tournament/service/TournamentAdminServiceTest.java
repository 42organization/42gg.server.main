package com.gg.server.admin.tournament.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.gg.server.admin.tournament.dto.TournamentAdminUpdateRequestDto;
import com.gg.server.domain.tournament.data.Tournament;
import com.gg.server.domain.tournament.data.TournamentGame;
import com.gg.server.domain.tournament.data.TournamentGameRepository;
import com.gg.server.domain.tournament.data.TournamentRepository;
import com.gg.server.domain.tournament.exception.TournamentConflictException;
import com.gg.server.domain.tournament.exception.TournamentNotFoundException;
import com.gg.server.domain.tournament.exception.TournamentUpdateException;
import com.gg.server.domain.tournament.type.TournamentRound;
import com.gg.server.domain.tournament.type.TournamentStatus;
import com.gg.server.domain.tournament.type.TournamentType;
import com.gg.server.global.exception.custom.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TournamentAdminServiceTest {
    @Mock
    TournamentRepository tournamentRepository;
    @Mock
    TournamentGameRepository tournamentGameRepository;
    @InjectMocks
    TournamentAdminService tournamentAdminService;

    @Nested
    @DisplayName("토너먼트 관리자 서비스 수정 테스트")
    class TournamentAdminServiceUpdateTest {
        @Test
        @DisplayName("토너먼트_업데이트_성공")
        public void success() {
            // given
            List<Tournament> tournamentList = makeTournaments(1L, 2, getTargetTime(2, 1));
            Tournament tournament = tournamentList.get(0);
            TournamentAdminUpdateRequestDto updateRequestDto = makeTournamentAdminUpdateRequestDto(
                getTargetTime(3, 1), getTargetTime(3, 3));
            given(tournamentRepository.findById(1L)).willReturn(Optional.of(tournament));
            given(tournamentRepository.findAllByStatusIsNot(TournamentStatus.END)).willReturn(tournamentList);
            given(tournamentRepository.save(any(Tournament.class))).willReturn(tournament);
            // when
            Tournament changedTournament = tournamentAdminService.updateTournamentInfo(1L, updateRequestDto);
            // then
            assertThat(changedTournament.getId()).isEqualTo(tournament.getId());
            assertThat(changedTournament.getTitle()).isEqualTo(updateRequestDto.getTitle());
            assertThat(changedTournament.getContents()).isEqualTo(updateRequestDto.getContents());
            assertThat(changedTournament.getStartTime()).isEqualTo(updateRequestDto.getStartTime());
            assertThat(changedTournament.getEndTime()).isEqualTo(updateRequestDto.getEndTime());
            assertThat(changedTournament.getType()).isEqualTo(updateRequestDto.getType());
            assertThat(changedTournament.getStatus()).isEqualTo(tournament.getStatus());
        }

        @Test
        @DisplayName("타겟_토너먼트_없음")
        public void tournamentNotFound() {
            // given
            Tournament tournament = makeTournament(1234L, TournamentStatus.BEFORE,
                getTargetTime(2, 1), getTargetTime(2, 3));
            TournamentAdminUpdateRequestDto updateRequestDto = makeTournamentAdminUpdateRequestDto(
                getTargetTime(2, 1), getTargetTime(2, 3));

            given(tournamentRepository.findById(tournament.getId())).willReturn(Optional.empty());
            // when, then
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournament.getId(), updateRequestDto))
                .isInstanceOf(TournamentNotFoundException.class);
        }

        @Test
        @DisplayName("토너먼트_업데이트_불가_상태")
        public void canNotUpdate() {
            // given
            Tournament tournamentLive = makeTournament(1L, TournamentStatus.LIVE,
                getTargetTime(0, -1), getTargetTime(0, 1));
            Tournament tournamentEnd = makeTournament(2L, TournamentStatus.END,
                getTargetTime(0, -3), getTargetTime(0, -1));
            TournamentAdminUpdateRequestDto updateRequestDto = makeTournamentAdminUpdateRequestDto(
                getTargetTime(2, 1), getTargetTime(2, 3));
            given(tournamentRepository.findById(tournamentLive.getId())).willReturn(Optional.of(tournamentLive));
            given(tournamentRepository.findById(tournamentEnd.getId())).willReturn(Optional.of(tournamentEnd));
            // when, then
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournamentLive.getId(), updateRequestDto))
                .isInstanceOf(TournamentUpdateException.class);
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournamentEnd.getId(), updateRequestDto))
                .isInstanceOf(TournamentUpdateException.class);
        }

        @Test
        @DisplayName("업데이트_토너먼트_Dto_Invalid_Time")
        public void Dto_Invalid_Time() {
            // given
            Tournament tournament = makeTournament(1L, TournamentStatus.BEFORE,
                getTargetTime(2, 1), getTargetTime(2, 3));
            TournamentAdminUpdateRequestDto invalidRequestDto1 = makeTournamentAdminUpdateRequestDto(
                getTargetTime(2, 3), getTargetTime(2, 1));
            TournamentAdminUpdateRequestDto invalidRequestDto2 = makeTournamentAdminUpdateRequestDto(
                invalidRequestDto1.getStartTime(), invalidRequestDto1.getStartTime());
            TournamentAdminUpdateRequestDto invalidRequestDto3 = makeTournamentAdminUpdateRequestDto(
                getTargetTime(2, -1), getTargetTime(2, 1));

            given(tournamentRepository.findById(1L)).willReturn(Optional.of(tournament));
            // when then
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournament.getId(), invalidRequestDto1))
                .isInstanceOf(InvalidParameterException.class);
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournament.getId(), invalidRequestDto2))
                .isInstanceOf(InvalidParameterException.class);
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournament.getId(), invalidRequestDto3))
                .isInstanceOf(InvalidParameterException.class);
        }

        @Test
        @DisplayName("Dto_기간_토너먼트_기간_겹침")
        public void tournamentTimeConflict() {
            // given
            List<Tournament> tournamentList = makeTournaments(1L, 2, getTargetTime(2, 1));
            Tournament tournament = tournamentList.get(0);
            TournamentAdminUpdateRequestDto updateRequestDto = makeTournamentAdminUpdateRequestDto(
                LocalDateTime.now().plusDays(2).plusHours(3), LocalDateTime.now().plusDays(2).plusHours(5));
            given(tournamentRepository.findById(1L)).willReturn(Optional.of(tournament));
            given(tournamentRepository.findAllByStatusIsNot(TournamentStatus.END)).willReturn(tournamentList);
            // when, then
            assertThatThrownBy(() -> tournamentAdminService.updateTournamentInfo(tournament.getId(), updateRequestDto))
                .isInstanceOf(TournamentConflictException.class);
        }
    }

    // 토너먼트 삭제 서비스 테스트
    @Nested
    @DisplayName("토너먼트 관리자 서비스 삭제 테스트")
    class TournamentAdminServiceDeleteTest {
        @Test
        @DisplayName("토너먼트_삭제_성공")
        void success() {
            // given
            int tournamentGameCnt = 7;
            Tournament tournament = makeTournament(1L, TournamentStatus.BEFORE,
                getTargetTime(2, 1), getTargetTime(2, 3));
            List<TournamentGame> tournamentGameList = makeTournamentGames(1L, tournament, tournamentGameCnt);
            given(tournamentRepository.findById(1L)).willReturn(Optional.of(tournament));
            given(tournamentGameRepository.findAllByTournamentId(tournament.getId())).willReturn(tournamentGameList);
            // when, then
            tournamentAdminService.deleteTournamentInfo(tournament.getId());
        }
        @Test
        @DisplayName("타겟_토너먼트_없음")
        public void tournamentNotFound() {
            // given
            Tournament tournament = makeTournament(1L, TournamentStatus.BEFORE,
                getTargetTime(2, 1), getTargetTime(2, 3));
            given(tournamentRepository.findById(1L)).willReturn(Optional.empty());
            // when, then
            assertThatThrownBy(() -> tournamentAdminService.deleteTournamentInfo(tournament.getId()))
                .isInstanceOf(TournamentNotFoundException.class);
        }

        @Test
        @DisplayName("토너먼트_삭제_불가_상태")
        public void canNotDelete() {
            // given
            Tournament liveTournament = makeTournament(1L, TournamentStatus.LIVE,
                getTargetTime(0, -1), getTargetTime(0, 1));
            Tournament endTournament = makeTournament(1L, TournamentStatus.END,
                getTargetTime(-2, 5), getTargetTime(-2, 7));
            given(tournamentRepository.findById(liveTournament.getId())).willReturn(Optional.of(liveTournament));
            given(tournamentRepository.findById(endTournament.getId())).willReturn(Optional.of(endTournament));
            // when, then
            assertThatThrownBy(() -> tournamentAdminService.deleteTournamentInfo(liveTournament.getId()))
                .isInstanceOf(TournamentUpdateException.class);
            assertThatThrownBy(() -> tournamentAdminService.deleteTournamentInfo(endTournament.getId()))
                .isInstanceOf(TournamentUpdateException.class);
        }

    }

    /**
     * 현재 시간에서 days hours, 만큼 차이나는 시간을 구한다.
     * @param days
     * @param hours
     * @return
     */
    private LocalDateTime getTargetTime(long days, long hours) {
        return LocalDateTime.now().plusDays(days).plusHours(hours);
    }

    /**
     * 각 매개변수로 초기화 된 토너먼트를 반환
     * @param id
     * @param status
     * @param startTime
     * @param endTime
     * @return
     */
    private Tournament makeTournament(Long id, TournamentStatus status, LocalDateTime startTime, LocalDateTime endTime) {
        return new Tournament(
            id,
            id + "st tournament",
            "",
            startTime,
            endTime,
            TournamentType.ROOKIE,
            status,
            null,
            null,
            null
            );
    }

    /**
     * <div>id 부터 cnt개 만큼의 토너먼트 리스트를 반환해준다.<div/>
     * 각 토너먼트는 1시간 길이이며, 토너먼트간 1시간의 간격이 있다.
     * @param id
     * @param cnt
     * @param startTime
     * @return
     */
    private List<Tournament> makeTournaments(Long id, long cnt, LocalDateTime startTime) {
        List<Tournament> tournamentList = new ArrayList<>();
        for (long i=0; i<cnt; i++) {
            tournamentList.add(makeTournament(id++, TournamentStatus.BEFORE,
                startTime.plusHours(i*2), startTime.plusHours((i*2+1))));
        }
        return tournamentList;
    }

    /**
     * 각 매개변수로 초기화된 TournamentAdminUpdateRequestDto를 반환
     * @param startTime
     * @param endTime
     * @return
     */
    private TournamentAdminUpdateRequestDto makeTournamentAdminUpdateRequestDto(LocalDateTime startTime, LocalDateTime endTime) {
        return new TournamentAdminUpdateRequestDto(
            "tournament changed",
            "changed",
            startTime,
            endTime,
            TournamentType.ROOKIE
        );
    }

    /**
     * cnt 사이즈의 토너먼트 게임 리스트 생성
     * @param id 토너먼트 게임 id
     * @param tournament 해당 토너먼트
     * @param cnt 토너먼트 게임 수
     * @return
     */
    private List<TournamentGame> makeTournamentGames(Long id, Tournament tournament, int cnt) {
        List<TournamentGame> tournamentGameList = new ArrayList<>();
        TournamentRound [] values = TournamentRound.values();
        while (--cnt >= 0) {
            tournamentGameList.add(new TournamentGame(id, null, tournament, values[cnt]));
        }
        return tournamentGameList;
    }
}