package com.ykb.tools.admin.cache;

import java.util.Map;

import org.springframework.cache.CacheManager;

/**
 * Description of a {@link CacheManager}, primarily intended for serialization to
 * JSON.
 */
public class CacheManagerDescriptor {

	private Map<String, CacheDescriptor> caches;

	public Map<String, CacheDescriptor> getCaches() {
		return caches;
	}

	public void setCaches(Map<String, CacheDescriptor> caches) {
		this.caches = caches;
	}



}

