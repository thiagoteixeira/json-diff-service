package com.thiagoteixeira.jsondiffbusiness.config;

import com.thiagoteixeira.jsondiffbusiness.config.properties.HttpClientProperties;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration to allow HTTP client resource creation. It is responsible to provide some
 * Spring beans for the Apache HTTP client.
 *
 * @author thiagoteixeira
 */
@Configuration
public class RestConfig {

  private final HttpClientProperties properties;

  @Autowired
  public RestConfig(
      final HttpClientProperties properties) {
    this.properties = properties;
  }

  /**
   * Provide a {@link RestTemplate} instance following the configurations from
   * {@link HttpClientProperties} to the {@link org.apache.http.conn.HttpClientConnectionManager} implementation.
   *
   * @param builder The {@link RestTemplateBuilder} instance.
   * @return A {@link RestTemplate} instance.
   */
  @Primary
  @Bean
  public RestTemplate restTemplate(final RestTemplateBuilder builder) {
    final var httpClient = HttpClientBuilder
        .create()
        .setMaxConnTotal(this.properties.getMaxConnections())
        .setMaxConnPerRoute(this.properties.getMaxConnPerRoute())
        .build();

    final var factory = new HttpComponentsClientHttpRequestFactory(
        httpClient);
    factory.setReadTimeout(this.properties.getReadTimeout());
    factory.setConnectTimeout(this.properties.getConnectionTimeout());

    return new RestTemplate(factory);
  }
}
