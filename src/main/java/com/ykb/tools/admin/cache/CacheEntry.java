package com.ykb.tools.admin.cache;

/**
 * Description of a {@link Cache}, primarily intended for serialization to JSON.
 */
public class CacheEntry extends CacheDescriptor {

	private String name;

	private String cacheManager;

	public String getName() {
		return this.name;
	}

	public String getCacheManager() {
		return this.cacheManager;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCacheManager(String cacheManager) {
		this.cacheManager = cacheManager;
	}

}
