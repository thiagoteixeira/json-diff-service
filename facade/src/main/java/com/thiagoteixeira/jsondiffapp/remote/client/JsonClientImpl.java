package com.thiagoteixeira.jsondiffapp.remote.client;

import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.util.ApiHeaders;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * {@inheritDoc}
 * @see JsonClient
 *
 * @author thiagoteixeira
 */
@Component
public class JsonClientImpl implements JsonClient {

  private static final Logger logger = LoggerFactory.getLogger(JsonClientImpl.class);

  private static final String DATA_RIGHT_PATH = "/right";

  private static final String DATA_LEFT_PATH = "/left";

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
   * @see JsonClient#save(long, JsonSide, DataRequest)
   */
  @Override
  public Optional<DataResponse> save(long id, final JsonSide side, final DataRequest request) {
    final var requestEntity = new HttpEntity<>(request, buildHeaders());
    var result  = this.execute(buildSaveUri(side, id), HttpMethod.POST, requestEntity, DataResponse.class);
    return Optional.ofNullable(result.getBody());
  }

  /**
   * {@inheritDoc}
   * @see JsonClient#diff(long)
   */
  @Override
  public Optional<BusinessResponse> diff(long id) {
    logger.info("Start retrieving diff for JSON entity id '{}' in json-diff-business microservice", id);
    try {
      final var requestEntity = new HttpEntity<>(null, buildHeaders());
      var result  = this.execute(this.businessUri, HttpMethod.GET, requestEntity, BusinessResponse.class, id);
      logger.info("End retrieving diff for JSON entity id '{}' in json-diff-data microservice", id);
      return Optional.of(result.getBody());
    } catch (final HttpClientErrorException e){
      if(HttpStatus.NOT_FOUND.equals(e.getStatusCode())){
        logger.info("End retrieving diff JSON entity id '{}' in json-diff-data microservice but it was not found", id);
        return Optional.empty();
      }
      logger.error(e.getMessage(), e);
    }
    return Optional.empty();
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

  /**
   * Build the request URI to save left or right side into json-diff-data microservice.
   *
   * @param side The JSON side
   * @param args The request arguments
   * @return The URI built
   */
  String buildSaveUri(final JsonSide side, final Object...args){
    final var builder = UriComponentsBuilder.fromHttpUrl(this.dataUri);
    if(JsonSide.LEFT.equals(side)){
      builder.path(DATA_LEFT_PATH);
    } else {
      builder.path(DATA_RIGHT_PATH);
    }
    if(args != null && args.length > 0){
      return builder.build().expand(args).toUriString();
    }
    return builder.build().expand(args).toUriString();
  }
}
