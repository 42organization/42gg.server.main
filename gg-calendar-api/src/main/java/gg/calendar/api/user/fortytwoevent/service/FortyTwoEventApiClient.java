package gg.calendar.api.user.fortytwoevent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import gg.auth.FortyTwoAuthUtil;
import gg.calendar.api.user.fortytwoevent.controller.response.FortyTwoEventResponse;
import gg.utils.external.ApiUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FortyTwoEventApiClient {
	private final ApiUtil apiUtil;
	private final FortyTwoAuthUtil fortyTwoAuthUtil;

	@Value("https://api.intra.42.fr/v2/campus/29/events")
	private String eventUrl;

	public List<FortyTwoEventResponse> getEvents() {
		ParameterizedTypeReference<List<FortyTwoEventResponse>> responseType = new ParameterizedTypeReference<>() {
		};

		try {
			String accessToken = fortyTwoAuthUtil.getClientCredentialToken();
			return apiUtil.callApiWithAccessTokenEvent(eventUrl, accessToken, responseType);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				String refreshToken = fortyTwoAuthUtil.getClientCredentialToken();
				return apiUtil.callApiWithAccessTokenEvent(eventUrl, refreshToken, responseType);
			}
			throw e;
		}
	}
}
