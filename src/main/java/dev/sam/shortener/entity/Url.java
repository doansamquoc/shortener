package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "urls", indexes = {@Index(columnList = "shortCode")})
public class Url extends Base {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@Column(name = "actual_url")
	String actualUrl;

	@Column(name = "short_code")
	String shortCode;

	@Column(name = "title")
	String title;

	@Builder.Default
	@Column(name = "total_clicks")
	Long totalClicks = 0L;

	@Column(name = "last_click_at")
	Instant lastClickAt;

	@Builder.Default
	@OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Click> clicks = new ArrayList<>();
}
