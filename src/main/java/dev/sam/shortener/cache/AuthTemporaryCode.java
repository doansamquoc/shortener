package dev.sam.shortener.cache;

public interface AuthTemporaryCode {
	void add(String code, Object value);

	Object get(String code);

	void remove(String code);
}
