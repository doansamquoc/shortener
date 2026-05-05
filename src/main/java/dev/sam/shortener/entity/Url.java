package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

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

	@Column(nullable = false, columnDefinition = "TEXT")
	String originalUrl;

	@Column(nullable = false, length = 10)
	String shortCode;

	@Builder.Default
	@Column(name = "total_clicks")
	Long totalClicks = 0L;

	@OneToMany(mappedBy = "url", cascade = CascadeType.ALL, orphanRemoval = true)
	List<Click> clicks = new ArrayList<>();

	// Just for search
	@Column(name = "search_vector", columnDefinition = "tsvector", insertable = false, updatable = false)
	String searchVector;
}
