package com.gg.server.domain.user.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gg.server.data.game.Season;
import com.gg.server.data.game.redis.RankRedis;
import com.gg.server.data.user.User;
import com.gg.server.domain.rank.exception.RedisDataNotFoundException;
import com.gg.server.domain.rank.redis.RankRedisRepository;
import com.gg.server.domain.rank.redis.RedisKeyManager;
import com.gg.server.domain.season.service.SeasonFindService;
import com.gg.server.domain.user.data.UserRepository;
import com.gg.server.domain.user.exception.UserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserFindService {
	private final UserRepository userRepository;
	private final SeasonFindService seasonFindService;
	private final RankRedisRepository rankRedisRepository;

	@Transactional(readOnly = true)
	public User findUserById(Long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public User findByIntraId(String intraId) {
		return userRepository.findByIntraId(intraId).orElseThrow(UserNotFoundException::new);
	}

	@Transactional(readOnly = true)
	public String getUserStatusMessage(User targetUser) {
		Season currentSeason = seasonFindService.findCurrentSeason(LocalDateTime.now());
		String hashKey = RedisKeyManager.getHashKey(currentSeason.getId());
		try {
			RankRedis userRank = rankRedisRepository.findRankByUserId(hashKey, targetUser.getId());
			return userRank.getStatusMessage();
		} catch (RedisDataNotFoundException e) {
			return "";
		}
	}
}
