package com.dubaidrums.jems.service;

public interface JemsCacheService {
	public void set(String key, String val);
	public void setup();
	public String get(String key);
}
