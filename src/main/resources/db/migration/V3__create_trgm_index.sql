CREATE INDEX idx_url_title_trgm ON urls USING GIN (title gin_trgm_ops);
CREATE INDEX idx_url_actual_trgm ON urls USING GIN (actual_url gin_trgm_ops);