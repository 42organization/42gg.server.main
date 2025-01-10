package gg.api42.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.api42.ApiClient;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class ApiController {
	private final ApiClient apiClient;

	@GetMapping("/token")
	public String getToken() {
		return apiClient.getToken();
	}
}
