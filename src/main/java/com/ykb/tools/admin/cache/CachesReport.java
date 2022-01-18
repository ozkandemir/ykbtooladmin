package com.ykb.tools.admin.cache;

import java.util.Map;

/**
 * A report of available {@link Cache caches}, primarily intended for
 * serialization to JSON.
 */
public class CachesReport {

	private Map<String, CacheManagerDescriptor> cacheManagers;

	public Map<String, CacheManagerDescriptor> getCacheManagers() {
		return cacheManagers;
	}

	public void setCacheManagers(Map<String, CacheManagerDescriptor> cacheManagers) {
		this.cacheManagers = cacheManagers;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(CachesReport.class)) {
			CachesReport rep = (CachesReport) obj;
			if (rep.getCacheManagers() != null && cacheManagers != null) {
				return rep.getCacheManagers().keySet().equals(cacheManagers.keySet());
			}
		}
		return super.equals(obj);
	}

	@Override
	public int hashCode() {

		return cacheManagers.keySet().hashCode();
	}

}