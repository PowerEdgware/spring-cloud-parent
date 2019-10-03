package com.study.loadbalance;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.scheduling.annotation.Scheduled;

//RestTemplate
public class CustomRestTemplateInterceptor implements ClientHttpRequestInterceptor {

	@Autowired
	private DiscoveryClient discoveryClient;

	volatile Map<String, Set<URI>> targetURI = new LinkedHashMap<String, Set<URI>>();

	@Scheduled(initialDelay = 100, fixedDelay = 5 * 1000)
	public void updateTargetURL() {
		Map<String, Set<URI>> newTargetURI = new LinkedHashMap<String, Set<URI>>();
		discoveryClient.getServices().forEach((serviceName) -> {
			List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
//			Set<URI> serviceURISet=instances.stream().map(instance ->
//				instance.getUri()
//			).collect(Collectors.toSet());
			instances.forEach(instance -> {
				newTargetURI.computeIfAbsent(serviceName, uriSet -> new LinkedHashSet<>()).add(instance.getUri());
			});
		});

		this.targetURI = newTargetURI;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		// 获取requestURI ,主要是获取ServiceName
		URI uri = request.getURI();
		String path = uri.getPath();// pattern:/serviveName/say
		String parts[] = path.substring(1).split("/");
		String serviceName = parts[0];
		String shortPath = "/" + parts[1];
		String queryString = uri.getQuery();
		URI realServiceUri = getRealURI(serviceName);

		//TODO 必须对中文参数进行编码，否则请求会失败:serverStatuscode=400
		String fullInvokeURL = realServiceUri.getScheme() + "://" + realServiceUri.getHost() + ":"
				+ realServiceUri.getPort() + shortPath + "?" + queryString;
		System.out.println("fullInvokeURL=" + fullInvokeURL);

		URI wrapperedURI = URI.create(fullInvokeURL);

		// replace uri's serviceName to real host,port and so on
		// TODO request的URI不能设置新的值，所以只能重新包装Request

		return execution.execute(URIRequestWrapper.create(request, wrapperedURI), body);
	}

	private URI getRealURI(String serviceName) {
		Set<URI> serviceURI = this.targetURI.get(serviceName);
		List<URI> uriList = new ArrayList<>(serviceURI);

		// Random return
		Random rnd = new Random();
		int index = rnd.nextInt(uriList.size());
		return uriList.get(index);
	}

	static class URIRequestWrapper implements HttpRequest {
		private HttpRequest request;
		private URI warpperedURI;

		static URIRequestWrapper create(HttpRequest request, URI newURI) {
			return new URIRequestWrapper(request, newURI);
		}

		private URIRequestWrapper(HttpRequest request, URI newURI) {
			this.request = request;
			this.warpperedURI = newURI;
		}

		@Override
		public HttpHeaders getHeaders() {
			return request.getHeaders();
		}

		@Override
		public String getMethodValue() {
			return request.getMethodValue();
		}

		@Override
		public URI getURI() {
			// TODO 返回包装后的URI
			return warpperedURI;
		}

	}
}
