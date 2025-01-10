package gg.auth;

import static gg.utils.exception.ErrorCode.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import gg.utils.exception.ErrorCode;
import gg.utils.exception.custom.NotExistException;
import gg.utils.exception.user.TokenNotValidException;
import gg.utils.external.ApiUtil;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FortyTwoAuthUtil {
	private final ApiUtil apiUtil;
	private final OAuth2AuthorizedClientService authorizedClientService;

	public String getAccessToken() {
		Authentication authentication = getAuthenticationFromContext();
		OAuth2AuthorizedClient client = getClientFromAuthentication(authentication);
		if (Objects.isNull(client)) {
			throw new TokenNotValidException();
		}
		return client.getAccessToken().getTokenValue();
	}

	public String getClientToken(String clientId, String clientSecret, String tokenUri) {

		Map<String, String> parameters = new HashMap<>();
		parameters.put("grant_type", "client_credentials");
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);

		// HTTP 요청 헤더 설정
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// 요청 바디를 JSON으로 변환
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters, headers);

		// ApiUtil의 apiCall 메서드 호출
		Map<String, Object> response = apiUtil.apiCall(
			tokenUri,       // URL
			Map.class,      // 응답 타입
			headers,        // HTTP 헤더
			requestEntity.getBody(), // 요청 바디 (JSON 파라미터)
			HttpMethod.POST // HTTP 메서드
		);

		if (Objects.isNull(response) || response.isEmpty()) {
			throw new NotExistException(ErrorCode.AUTH_NOT_FOUND);
		}
		return ((String)response.get("access_token"));
	}

	/**
	 * 토큰 갱신
	 * @return 갱신된 OAuth2AuthorizedClient
	 */
	public String refreshAccessToken() {
		Authentication authentication = getAuthenticationFromContext();
		OAuth2AuthorizedClient client = getClientFromAuthentication(authentication);
		ClientRegistration registration = client.getClientRegistration();

		OAuth2AuthorizedClient newClient = requestNewClient(client, registration);

		authorizedClientService.removeAuthorizedClient(
			registration.getRegistrationId(), client.getPrincipalName());
		authorizedClientService.saveAuthorizedClient(newClient, authentication);

		return newClient.getAccessToken().getTokenValue();
	}

	private Authentication getAuthenticationFromContext() {
		SecurityContext context = SecurityContextHolder.getContext();
		return context.getAuthentication();
	}

	private OAuth2AuthorizedClient getClientFromAuthentication(Authentication authentication) {
		OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken)authentication;
		String registrationId = oauthToken.getAuthorizedClientRegistrationId();
		return authorizedClientService.loadAuthorizedClient(registrationId, oauthToken.getName());
	}

	private OAuth2AuthorizedClient requestNewClient(OAuth2AuthorizedClient client, ClientRegistration registration) {
		if (Objects.isNull(client.getRefreshToken())) {
			throw new NotExistException(AUTH_NOT_FOUND);
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> params = new HashMap<>();
		params.put("grant_type", "refresh_token");
		params.put("refresh_token", client.getRefreshToken().getTokenValue());
		params.put("client_id", registration.getClientId());
		params.put("client_secret", registration.getClientSecret());
		params.put("redirect_uri", registration.getRedirectUri());

		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(params, headers);

		List<Map<String, Object>> responseBody = apiUtil.apiCall(
			registration.getProviderDetails().getTokenUri(),
			List.class,
			headers,
			requestEntity.getBody(),
			HttpMethod.POST
		);
		if (Objects.isNull(responseBody) || responseBody.isEmpty()) {
			throw new NotExistException(ErrorCode.AUTH_NOT_FOUND);
		}
		return createNewClientFromApiResponse(responseBody.get(0), client);
	}

	private OAuth2AuthorizedClient createNewClientFromApiResponse(
		Map<String, Object> response, OAuth2AuthorizedClient client) {

		OAuth2AccessToken newAccessToken = new OAuth2AccessToken(
			OAuth2AccessToken.TokenType.BEARER,
			(String)response.get("access_token"),
			Instant.now(),
			Instant.now().plusSeconds((Integer)response.get("expires_in"))
		);

		OAuth2RefreshToken newRefreshToken = new OAuth2RefreshToken(
			(String)response.get("refresh_token"),
			Instant.now()
		);

		return new OAuth2AuthorizedClient(
			client.getClientRegistration(),
			client.getPrincipalName(),
			newAccessToken,
			newRefreshToken
		);
	}
}
