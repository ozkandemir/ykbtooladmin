package com.ykb.tools.admin.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ykb.tools.admin.cache.CacheManagerDescriptor;
import com.ykb.tools.admin.cache.CachesReport;
import com.ykb.tools.admin.cache.service.ICacheService;

import de.codecentric.boot.admin.server.domain.entities.Application;
import de.codecentric.boot.admin.server.domain.entities.Instance;

@Component
@RestController
@RequestMapping("toolsApps")
public class AdminController {

	@Autowired
	private ICacheService cacheService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/application/{applicationName}/instance")
	public List<Instance> getInstances(@PathVariable String applicationName) {
		Optional<Application> application = cacheService.getApplications().stream()
				.filter(app -> app.getName().equals(applicationName)).findFirst();
		return cacheService.getInstances(application.get());
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/application/{applicationName}/instance/{instanceId}/cache")
	public CachesReport getCaches(@PathVariable String applicationName, @PathVariable String instanceId) {
		Optional<Application> application = cacheService.getApplications().stream()
				.filter(app -> app.getName().equals(applicationName)).findFirst();

		Optional<Instance> instance = cacheService.getInstances(application.get()).stream()
				.filter(ins -> ins.getId().getValue().equals(instanceId)).findFirst();

		return cacheService.getCaches(application.get(), instance.get());
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/application")
	public List<Application> getApplications() {
		return cacheService.getApplications();
	}
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/application/{applicationName}/cache")
	public List<String> getCaches(@PathVariable String applicationName){
		
		Optional<Application> application = cacheService.getApplications().stream()
				.filter(app -> app.getName().equals(applicationName)).findFirst();
		
		
		List<String> allCaches = cacheService.getInstances(application.get())
			.parallelStream()
			.map(ins -> {
				return cacheService.getCaches(application.get(), ins);
				}
			)
			.distinct()
			.map(cacheReport -> {
				List<CacheManagerDescriptor> descriptors = new ArrayList<>(cacheReport.getCacheManagers().values());
				List<String> cacheKeys = new ArrayList<>();
				for (CacheManagerDescriptor cacheManagerDescriptor : descriptors) {
					cacheKeys.addAll(cacheManagerDescriptor.getCaches().keySet());
				}
				return cacheKeys;
			})
			.collect(ArrayList::new, List::addAll, List::addAll);
	
		return allCaches;
	}
	
	@DeleteMapping( produces = MediaType.APPLICATION_JSON_VALUE, path = "/application/{applicationName}/cache/{cacheName}")
	public Map<String,Boolean> refreshCache(@PathVariable String applicationName, @PathVariable String cacheName){
		Optional<Application> application = cacheService.getApplications().stream()
				.filter(app -> app.getName().equals(applicationName)).findFirst();
		
		HashMap<String, Boolean> response = new HashMap<>();
		
		cacheService.getInstances(application.get())
			.parallelStream()
			.forEach(instance -> {
				response.put(instance.getId().getValue(), cacheService.refreshCache(instance, cacheName)); 
			});
			
		return response;
		
	}

}
