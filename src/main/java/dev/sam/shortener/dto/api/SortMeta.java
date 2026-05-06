package dev.sam.shortener.dto.api;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SortMeta {
	String sortBy;
	Sort.Direction direction;
}
