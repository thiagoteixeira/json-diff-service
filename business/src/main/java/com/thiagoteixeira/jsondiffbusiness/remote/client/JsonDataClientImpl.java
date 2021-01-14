package com.thiagoteixeira.jsondiffbusiness.remote.client;

import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import com.thiagoteixeira.jsondiffbusiness.util.ApiHeaders;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * {@inheritDoc}
 * @see JsonDataClient
 *
 * @author thiagoteixeira
 */
@Component
public class JsonDataClientImpl implements JsonDataClient {

  private static final Logger logger = LoggerFactory.getLogger(JsonDataClientImpl.class);

  @Value("${microservices.data.uri}")
  private String uri;

  private final RestTemplate template;

  @Autowired
  public JsonDataClientImpl(RestTemplate template) {
    this.template = template;
  }

  /**
   * {@inheritDoc}
   * @see JsonDataClient#find(long) 
   */
  @Override
  public Optional<JsonDataResponse> find(long id) {
    logger.info("Start retrieving JSON entity id '{}' in json-diff-data microservice", id);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, MDC.get(ApiHeaders.TRACE_ID_HEADER));
    final var requestEntity = new HttpEntity<>(null, headers);
    var result  = this.template.exchange(this.uri, HttpMethod.GET, requestEntity, JsonDataResponse.class, id);

    logger.info("End retrieving JSON entity id '{}' in json-diff-data microservice", id);
    return Optional.ofNullable(result.getBody());
  }
}
