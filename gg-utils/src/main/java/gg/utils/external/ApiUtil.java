package gg.utils.external;

import java.util.List;
import java.util.Map;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gg.utils.exception.user.TokenNotValidException;

@Component
public class ApiUtil {
	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;

	public ApiUtil(ObjectMapper objectMapper, RestTemplateBuilder restTemplateBuilder) {
		this.objectMapper = objectMapper;
		this.restTemplate = restTemplateBuilder.build();
	}

	public <T> T apiCall(String url, Class<T> responseType, HttpHeaders headers,
		MultiValueMap<String, String> parameters, HttpMethod method) {
		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);
		ResponseEntity<T> res = restTemplate.exchange(url, method, request, responseType);
		if (!res.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("api call error");
		}
		return res.getBody();
	}

	public <T> T apiCall(String url, Class<T> responseType, HttpHeaders headers,
		Map<String, String> bodyJson, HttpMethod method) {
		String contentBody = null;
		try {
			contentBody = objectMapper.writeValueAsString(bodyJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		HttpEntity<String> request = new HttpEntity<>(contentBody, headers);
		ResponseEntity<T> res = restTemplate.exchange(url, method, request, responseType);
		if (!res.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("api call error");
		}
		return res.getBody();
	}

	public <T> T apiCall(String url, Class<T> responseType, HttpHeaders headers, HttpMethod method) {
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<T> res = restTemplate.exchange(url, method, request, responseType);
		if (!res.getStatusCode().is2xxSuccessful()) {
			throw new TokenNotValidException();
		}
		return res.getBody();
	}

	public <T> T apiCall(String url, ParameterizedTypeReference<T> responseType, HttpHeaders headers,
		HttpMethod method) {
		HttpEntity<String> request = new HttpEntity<>(headers);
		ResponseEntity<T> res = restTemplate.exchange(url, method, request, responseType);
		if (!res.getStatusCode().is2xxSuccessful()) {
			throw new TokenNotValidException();
		}
		return res.getBody();
	}

	/**
	 * API 호출
	 * @param url 호출할 URL
	 * @param accessToken 액세스 토큰
	 * @param responseType 응답 타입
	 * @return 응답
	 */
	public List<Map<String, String>> callApiWithAccessToken(String url, String accessToken,
		ParameterizedTypeReference<List<Map<String, String>>> responseType) {

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);

		return apiCall(url, responseType, headers, HttpMethod.GET);
	}

	public <T> T callApiWithAccessTokenEvent(String url, String accessToken,
		ParameterizedTypeReference<T> responseType) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(accessToken);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return apiCall(url, responseType, headers, HttpMethod.GET);
	}
}
