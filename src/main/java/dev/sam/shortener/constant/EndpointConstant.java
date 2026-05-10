package dev.sam.shortener.constant;

public class EndpointConstant {
	public static final String V1 = "/api/v1";
	public static final String[] PUBLIC_ENDPOINTS = {"/*", V1 + "/auth/**"};
	public static final String[] SWAGGER_ENDPOINTS = {"/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html"};
}
