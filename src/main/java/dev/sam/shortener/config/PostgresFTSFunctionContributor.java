package dev.sam.shortener.config;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.boot.model.FunctionContributor;
import org.hibernate.type.StandardBasicTypes;

public class PostgresFTSFunctionContributor implements FunctionContributor {
	@Override
	public void contributeFunctions(FunctionContributions contributions) {
		// Search
		contributions.getFunctionRegistry().registerPattern(
			"fts_match",
			"?1 @@ websearch_to_tsquery('simple',?2)",
			contributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.BOOLEAN)
		);

		// Sort
		contributions.getFunctionRegistry().registerPattern(
			"fts_rank",
			"fts_rank(?1, websearch_to_tsquery('simple',?2))",
			contributions.getTypeConfiguration().getBasicTypeRegistry().resolve(StandardBasicTypes.DOUBLE)
		);
	}
}
