package gg.api42.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.api42.ApiClient;

@RestController
@RequestMapping("/api/v1/events")
public class ApiEventController {
	private final ApiClient apiClient;

	@Autowired
	public ApiEventController(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	@GetMapping
	public String getEventList() {
		return apiClient.getEvents();
	}
}
