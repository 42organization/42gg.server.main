package gg.calendar.api.user.fortyTwoEvent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import gg.auth.FortyTwoAuthUtil;
import gg.calendar.api.user.fortyTwoEvent.controller.response.FortyTwoEventResponse;
import gg.repo.calendar.PublicScheduleRepository;
import gg.utils.external.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FortyTwoEventService {
	private final ApiUtil apiUtil;
	private final FortyTwoAuthUtil fortyTwoAuthUtil;
	private final PublicScheduleRepository publicScheduleRepository;

	@Value("https://api.intra.42.fr/v2/campus/29/events")
	private String eventUrl;

	// @Transactional
	// public void checkNewEvent() {
	// 	try {
	// 		List<FortyTwoEventResponse> events = getEvents();
	// 	} catch () {
	// 	}
	// }

	@Transactional(readOnly = true)
	public List<FortyTwoEventResponse> getEvents() {
		ParameterizedTypeReference<List<FortyTwoEventResponse>> responseType =
			new ParameterizedTypeReference<>() {
			};
		try {
			log.info("getEvent started===");
			String accessToken = fortyTwoAuthUtil.getAccessToken();
			log.info("---accessToken---: {}", accessToken);
			return apiUtil.callApiWithAccessTokenEvent(eventUrl, accessToken, responseType);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				String accessToken = fortyTwoAuthUtil.refreshAccessToken();
				return apiUtil.callApiWithAccessTokenEvent(eventUrl, accessToken, responseType);
			}
			throw e;
		}
	}
}
