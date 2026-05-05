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
@Table(name = "clickers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clicker extends Base {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "url_id")
	Url url;

	// IP address
	String ipAddress;

	// ID of browser
	String fingerprintId;

	// Browser name, OS name, e.g.
	String userAgent;

	// Where's click from
	String referrer;

	// Country code like VN, US, e.g.
	@Column(name = "country_code", length = 2)
	Character countryCode;
}
