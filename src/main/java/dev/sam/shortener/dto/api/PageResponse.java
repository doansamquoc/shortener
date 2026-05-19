package dev.sam.shortener.dto.api;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse<T> {
	List<T> content;
	PageMeta pageMeta;

	public static <T> PageResponse<T> from(Page<T> page) {
		List<SortMeta> sortMetas = page.getSort().stream().map(
			order -> SortMeta.builder().sortBy(order.getProperty()).direction(order.getDirection()).build()
		).toList();

		List<SortMeta> cleanSorts = new ArrayList<>(sortMetas);
		PageMeta meta = PageMeta.builder()
			.pageNumber(page.getNumber())
			.pageSize(page.getSize())
			.totalElements(page.getTotalElements())
			.totalPages(page.getTotalPages())
			.sort(cleanSorts)
			.build();

		return PageResponse.<T>builder().content(new ArrayList<>(page.getContent())).pageMeta(meta).build();
	}
}
