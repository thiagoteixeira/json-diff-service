package com.thiagoteixeira.jsondiffbusiness.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * HTTP client properties for Pooling connection manager that will be used during the {@link org.springframework.web.client.RestTemplate}
 * bean creation.
 * @see com.thiagoteixeira.jsondiffbusiness.config.RestConfig
 */
@Component
@ConfigurationProperties("rest.http")
public class HttpClientProperties {

  private int maxConnections;
  private int maxConnPerRoute;
  private int readTimeout;
  private int connectionTimeout;

  public int getMaxConnections() {
    return maxConnections;
  }

  public void setMaxConnections(int maxConnections) {
    this.maxConnections = maxConnections;
  }

  public int getMaxConnPerRoute() {
    return maxConnPerRoute;
  }

  public void setMaxConnPerRoute(int maxConnPerRoute) {
    this.maxConnPerRoute = maxConnPerRoute;
  }

  public int getReadTimeout() {
    return readTimeout;
  }

  public void setReadTimeout(int readTimeout) {
    this.readTimeout = readTimeout;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }
}
