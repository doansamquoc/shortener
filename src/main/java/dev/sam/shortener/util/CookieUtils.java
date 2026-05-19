package dev.sam.shortener.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

public class CookieUtils {
    public static void add(HttpServletResponse res, String name, String value, long maxAge, String path) {
        ResponseCookie cookie = ResponseCookie.from(name, value)
                                              .maxAge(maxAge)
                                              .path(path)
                                              .sameSite("Lax")
                                              .secure(false)
                                              .httpOnly(true)
                                              .build();
        
        res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    
    public static void remove(HttpServletResponse res, String name, String path) {
        add(res, name, null, -1, path);
    }
    
    public static String serialize(Object object) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(object));
    }
    
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(SerializationUtils.deserialize(Base64.getUrlDecoder().decode(cookie.getValue())));
    }
}
