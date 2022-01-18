package com.ykb.tools.admin.cache.service;

import java.util.List;

import com.ykb.tools.admin.cache.CachesReport;

import de.codecentric.boot.admin.server.domain.entities.Application;
import de.codecentric.boot.admin.server.domain.entities.Instance;

public interface ICacheService {

	List<Instance> getInstances(Application application);

	CachesReport getCaches(Application application, Instance instance);

	List<Application> getApplications();

	boolean refreshCache(Instance instance, String cacheName);

}