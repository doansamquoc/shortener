package dev.sam.shortener.controller;

import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.request.SendMailRequest;
import dev.sam.shortener.service.SendMailService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequestMapping(V1 + "/mails")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MailController {
	SendMailService service;

	@PostMapping("/simple")
	public ResponseEntity<ApiResponse<?>> sendSimpleMail(@Valid @RequestBody SendMailRequest request) {
		service.sendSimpleMail(request);
		return ResponseEntity.ok(ApiResponse.of("Simple mail sent successfully"));
	}
}
