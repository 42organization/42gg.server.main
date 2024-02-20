package gg.pingpong.repo.game;

import gg.pingpong.data.user.type.SnsType;

public interface GameUser {
	Long getGameId();

	Long getUserId();

	String getIntraId();

	String getEmail();

	SnsType getSnsNotiOpt();
}
