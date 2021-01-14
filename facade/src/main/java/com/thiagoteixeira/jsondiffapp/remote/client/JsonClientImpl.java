package com.thiagoteixeira.jsondiffapp.remote.client;

import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.util.ApiHeaders;
import java.util.Optional;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * {@inheritDoc}
 * @see JsonClient
 *
 * @author thiagoteixeira
 */
@Component
public class JsonClientImpl implements JsonClient {

  @Value("${microservices.business.uri}")
  private String businessUri;

  @Value("${microservices.data.uri}")
  private String dataUri;

  private final RestTemplate template;

  @Autowired
  public JsonClientImpl(final RestTemplate template) {
    this.template = template;
  }

  /**
   * {@inheritDoc}
   * @see JsonClient#create(long, DataRequest) 
   */
  @Override
  public Optional<DataResponse> create(long id, final DataRequest request) {
    final var requestEntity = new HttpEntity<>(request, buildHeaders());
    var result  = this.execute(this.dataUri, HttpMethod.POST, requestEntity, DataResponse.class, id);
    return Optional.ofNullable(result.getBody());
  }

  /**
   * {@inheritDoc}
   * @see JsonClient#diff(long)
   */
  @Override
  public Optional<BusinessResponse> diff(long id) {
    final var requestEntity = new HttpEntity<>(null, buildHeaders());
    var result  = this.execute(this.businessUri, HttpMethod.GET, requestEntity, BusinessResponse.class, id);
    return Optional.ofNullable(result.getBody());
  }

  /**
   * Build the requird HTTP headers for all the requests to json-diff-data and json-diff-business microservices.
   *
   * @return A {@link HttpHeaders} instance with all the required HTTP headers
   */
  private HttpHeaders buildHeaders() {
    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, MDC.get(ApiHeaders.TRACE_ID_HEADER));
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    return headers;
  }

  /**
   * Execute some HTTP call given the arguments
   *
   * @param uri The request URI
   * @param method The HTTP method
   * @param entity The request entity
   * @param responseType The response type
   * @param variables The request arguments
   * @param <T> The generic argument given a received type
   * @return A ResponseEntity encapsulation an instance of the received type argument
   */
  private <T> ResponseEntity<T> execute(final String uri, final HttpMethod method, final HttpEntity<?> entity, final Class<T> responseType, final Object... variables) {
    return this.template
        .exchange(uri, method, entity, responseType, variables);
  }

}
