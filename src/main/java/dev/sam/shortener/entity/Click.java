package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "clicks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Click extends Base {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "url_id")
	Url url;

	// IP address
	@Column(name = "ip_address")
	String ipAddress;

	// Browser name, OS name, etc
	@Column(name = "user_agent")
	String userAgent;

	// Where's click from
	@Column(name = "referer")
	String referrer;

	// Country code like VN, US, etc
	@Column(name = "country_code")
	String countryCode;
}
