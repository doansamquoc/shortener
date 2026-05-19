CREATE INDEX idx_click_country_code_trgm ON clicks USING GIN (country_code gin_trgm_ops);
CREATE INDEX idx_click_referrer_trgm ON clicks USING GIN (referer gin_trgm_ops);