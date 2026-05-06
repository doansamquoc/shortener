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
	String ipAddress;

	// Browser name, OS name, e.g.
	String userAgent;

	// Where's click from
	String referrer;

	// Country code like VN, US, e.g.
	@Column(name = "country_code", length = 2)
	Character countryCode;
}
