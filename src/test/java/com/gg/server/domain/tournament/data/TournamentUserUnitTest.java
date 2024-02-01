package com.gg.server.domain.tournament.data;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.gg.server.data.game.Tournament;
import com.gg.server.data.game.TournamentUser;
import com.gg.server.data.game.type.TournamentStatus;
import com.gg.server.data.game.type.TournamentType;
import com.gg.server.data.user.User;
import com.gg.server.utils.annotation.UnitTest;

@UnitTest
@DisplayName("TournamentUserUnitTest")
public class TournamentUserUnitTest {
	Tournament tournament;
	TournamentUser tournamentUser;
	User user;

	@Nested
	@DisplayName("DeleteTournament")
	class DeleteTournament {
		@Test
		@DisplayName("토너먼트 삭제 성공")
		void deleteSuccess() {
			//given
			user = Mockito.mock(User.class);
			tournament = new Tournament("", "", LocalDateTime.now(), LocalDateTime.now(),
				TournamentType.MASTER, TournamentStatus.END);
			tournamentUser = new TournamentUser(user, tournament, false, LocalDateTime.now());

			//when
			tournamentUser.deleteTournament();

			//then
			Assertions.assertNull(tournamentUser.getTournament());
		}
	}

	@Nested
	@DisplayName("UpdateIsJoined")
	class UpdateIsJoined {
		@Test
		@DisplayName("참가 정보 업데이트 성공")
		void updateSuccess() {
			//given
			tournamentUser = new TournamentUser(null, null, false, LocalDateTime.now());
			boolean isjoined = true;

			//when
			tournamentUser.updateIsJoined(isjoined);

			//then
			Assertions.assertEquals(isjoined, tournamentUser.getIsJoined());
		}
	}
}
