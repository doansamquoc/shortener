package dev.sam.shortener.controller;

import dev.sam.shortener.dto.api.ApiResponse;
import dev.sam.shortener.dto.api.PageResponse;
import dev.sam.shortener.dto.response.ClickResponse;
import dev.sam.shortener.service.ClickService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static dev.sam.shortener.constant.EndpointConstant.V1;

@RestController
@RequiredArgsConstructor
@RequestMapping(V1 + "/clicks")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClickController {
    ClickService service;
    
    @GetMapping("/{urlId}")
    ResponseEntity<ApiResponse<PageResponse<ClickResponse>>> findAll(
        @PathVariable Long urlId,
        @RequestParam(name = "q", defaultValue = "", required = false) String searchTerm,
        @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<ClickResponse> response = service.findAll(urlId, searchTerm, 0.3, pageable);
        return ResponseEntity.ok(ApiResponse.of(response));
    }
}
