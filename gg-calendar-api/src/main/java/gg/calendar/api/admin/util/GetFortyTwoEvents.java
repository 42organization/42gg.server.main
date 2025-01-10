package gg.calendar.api.admin.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import gg.auth.FortyTwoAuthUtil;
import gg.calendar.api.admin.util.controller.response.FortyTwoEventsResponse;
import gg.utils.external.ApiUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetFortyTwoEvents {
	private static final String GET_EVENT_URL = "https://api.intra.42.fr/v2/campus/29/events";

	@Value("${spring.security.oauth2.client.registration.42.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.42.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.provider.42.token-uri}")
	private String tokenUri;

	private final FortyTwoAuthUtil fortyTwoAuthUtil;

	private final ApiUtil apiUtil;

	public List<FortyTwoEventsResponse> getEvents() {
		ParameterizedTypeReference<List<FortyTwoEventsResponse>> responseType = new ParameterizedTypeReference<>() {
		};
		try {
			String accessToken = fortyTwoAuthUtil.getClientToken(clientId, clientSecret, tokenUri);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(accessToken);
			return apiUtil.apiCall(GET_EVENT_URL, responseType, headers, HttpMethod.GET);
		} catch (HttpClientErrorException e) {
			if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
				String accessToken = fortyTwoAuthUtil.refreshAccessToken();
				HttpHeaders headers = new HttpHeaders();
				headers.setBearerAuth(accessToken);
				return apiUtil.apiCall(GET_EVENT_URL, responseType, headers, HttpMethod.GET);
			}
			throw e;
		}
	}

}
