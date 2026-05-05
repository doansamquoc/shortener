package dev.sam.shortener.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class Base {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@CreationTimestamp
	Instant createdAt;

	@UpdateTimestamp
	Instant updatedAt;
}
