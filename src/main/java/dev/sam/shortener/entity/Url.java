package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "urls", indexes = {@Index(columnList = "shortCode")})
public class Url extends Base {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	User user;

	@Column(nullable = false, columnDefinition = "TEXT")
	String originalUrl;

	@Column(nullable = false, unique = true, length = 10)
	String shortCode;

	@Builder.Default
	@Column(name = "total_clicks")
	Long totalClicks = 0L;

	@OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Clicker> clickers = new ArrayList<>();
}
