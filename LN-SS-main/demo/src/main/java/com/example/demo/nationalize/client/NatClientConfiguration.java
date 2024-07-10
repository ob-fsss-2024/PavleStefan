package com.example.demo.nationalize.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class NatClientConfiguration {
    @Bean
    public NatClient natClient(@Value("${nat.api.url}") final String natApiUrl) {
        final RestClient restClient = RestClient.builder().baseUrl(natApiUrl).build();
        final RestClientAdapter adapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(NatClient.class);
    }
}
