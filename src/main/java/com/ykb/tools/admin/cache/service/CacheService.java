package com.ykb.tools.admin.cache.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.ykb.tools.admin.cache.CachesReport;

import de.codecentric.boot.admin.server.domain.entities.Application;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.values.Endpoint;
import de.codecentric.boot.admin.server.services.ApplicationRegistry;
import de.codecentric.boot.admin.server.web.client.InstanceWebClient;
import reactor.core.publisher.Mono;

@Service
public class CacheService implements ICacheService {
	@Autowired
	private ApplicationRegistry applicationRegistry;

	@Override
	public List<Instance> getInstances(Application application) {
		return application.getInstances();
	}

	@Override
	public CachesReport getCaches(Application application, Instance instance) {
		WebClient webClient = InstanceWebClient.builder().build().instance(instance);
		Endpoint cacheEndpoint = instance.getEndpoints().get("caches").get();
		Optional<CachesReport> response = webClient.get().uri(cacheEndpoint.getUrl()).accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(result -> {
					if (result.statusCode().value() == HttpStatus.SC_OK) {
						return result.bodyToMono(CachesReport.class);
					} else if (result.statusCode().is4xxClientError()) {
						return Mono.error(new RuntimeException());
					} else {
						return Mono.error(new RuntimeException());
					}
				}).blockOptional(Duration.ofSeconds(5));

		return response.get();
	}

	@Override
	public List<Application> getApplications() {
		List<Application> applications = new ArrayList<>();
		applicationRegistry.getApplications().subscribe(applications::add);

		return applications;
	}
	
	@Override
	public boolean refreshCache(Instance instance, String cacheName) {
		WebClient webClient = InstanceWebClient.builder().build().instance(instance);
		Endpoint cacheEndpoint = instance.getEndpoints().get("caches").get();
		
		Optional<Boolean> response;
		try {
			response = webClient.delete()
				.uri(cacheEndpoint.getUrl()+ "/" + cacheName)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(result -> {
					if(result.statusCode().is2xxSuccessful()) {
						return Mono.just(true);
					}else{
						return Mono.just(false);
					}
				}).blockOptional(Duration.ofSeconds(5));
		} catch (WebClientException e) {
			return false;
		}
			
		return response.get();
		
	}
}
