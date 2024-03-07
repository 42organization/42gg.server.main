package gg.party.api.user.templates.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gg.party.api.user.templates.controller.response.TemplatesResDto;
import gg.party.api.user.templates.service.TemplatesService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/party/templates")
public class TemplatesController {
	private final TemplatesService templatesService;

	/**
	 * 탬플릿 목록을 조회한다
	 * @return 탬플릿 전체 List
	 */
	@GetMapping
	public ResponseEntity<List<TemplatesResDto>> templateList() {
		return ResponseEntity.status(HttpStatus.OK).body(templatesService.findTemplatesList());
	}
}
