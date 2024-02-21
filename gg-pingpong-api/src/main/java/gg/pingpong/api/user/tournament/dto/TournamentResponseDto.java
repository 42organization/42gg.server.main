package gg.pingpong.api.user.tournament.dto;

import java.time.LocalDateTime;

import gg.pingpong.api.user.user.dto.UserImageDto;
import gg.pingpong.data.game.Tournament;
import gg.pingpong.data.game.TournamentUser;
import gg.pingpong.data.game.type.TournamentStatus;
import gg.pingpong.data.game.type.TournamentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@Getter
public class TournamentResponseDto {

	private Long tournamentId;
	private String title;
	private String contents;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private TournamentType type;
	private TournamentStatus status;
	private String winnerIntraId;
	private String winnerImageUrl;
	private long playerCnt;

	public TournamentResponseDto(Tournament tournament, UserImageDto winner, int playerCnt) {
		this.tournamentId = tournament.getId();
		this.title = tournament.getTitle();
		this.contents = tournament.getContents();
		this.startTime = tournament.getStartTime();
		this.endTime = tournament.getEndTime();
		this.type = tournament.getType();
		this.status = tournament.getStatus();
		this.winnerIntraId = winner.getIntraId();
		this.winnerImageUrl = winner.getImageUri();
		this.playerCnt = playerCnt;
	}

	public TournamentResponseDto(Tournament tournament) {
		this.tournamentId = tournament.getId();
		this.title = tournament.getTitle();
		this.contents = tournament.getContents();
		this.startTime = tournament.getStartTime();
		this.endTime = tournament.getEndTime();
		this.type = tournament.getType();
		this.status = tournament.getStatus();
		this.playerCnt = tournament.getTournamentUsers().stream()
			.filter(TournamentUser::getIsJoined).count();
		if (tournament.getWinner() != null) {
			this.winnerIntraId = tournament.getWinner().getIntraId();
			this.winnerImageUrl = tournament.getWinner().getImageUri();
		}
	}

	public void update_player_cnt(int playerCnt) {
		this.playerCnt = playerCnt;
	}
}
