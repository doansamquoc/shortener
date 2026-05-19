package dev.sam.shortener.service.impl;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import dev.sam.shortener.service.CountryCodeService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.net.InetAddress;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GeoIPServiceImpl implements CountryCodeService {
    DatabaseReader reader;
    
    @Override
    public String getCountryCode(String ip) {
        try {
            InetAddress ipAddress = InetAddress.getByName(ip);
            CountryResponse response = reader.country(ipAddress);
            return response.country().isoCode();
        } catch (Exception e) {
            return "Unknown";
        }
    }
}