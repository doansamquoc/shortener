package dev.sam.shortener.config.jwt;

import dev.sam.shortener.enums.ErrorCode;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Locale;

public interface AuthErrorWriter {
	void writeError(HttpServletResponse response, ErrorCode ec, String path) throws IOException;
}
