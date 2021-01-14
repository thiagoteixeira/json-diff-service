package com.thiagoteixeira.jsondiffbusiness.remote.client;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import com.thiagoteixeira.jsondiffbusiness.util.ApiHeaders;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Unit tests for {@link JsonDataClientImpl}
 *
 * @author thiagoteixeira
 */
@ExtendWith(MockitoExtension.class)
class JsonDataClientImplTest {

  private static final String DATA_SERVICE_URI = "http://127.0.0.1:8082";

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private JsonDataClientImpl client;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(this.client, "uri", DATA_SERVICE_URI);
  }

  @Test
  void findSuccessfully() {
    var expected = new JsonDataResponse();
    expected.setId(1L);
    expected.setLeft("foo");
    expected.setRight("bar");

    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate.exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L)).thenReturn(
        ResponseEntity.ok(expected));

    final Optional<JsonDataResponse> response = this.client.find(1l);

    assertTrue(response.isPresent());
    assertSame(expected, response.get());
    verify(this.restTemplate, times(1)).exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L);

    MDC.clear();
  }

  @Test
  void findNotFoundFromDataNotFoundResponse() {
    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate.exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L)).thenThrow(
        new HttpClientErrorException(
            HttpStatus.NOT_FOUND));

    final Optional<JsonDataResponse> response = this.client.find(1l);

    assertTrue(response.isEmpty());
    verify(this.restTemplate, times(1)).exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L);

    MDC.clear();
  }

  @Test
  void findNotFoundFromDataInternalServerErrorResponse() {
    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate.exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L)).thenThrow(
        new HttpClientErrorException(
            HttpStatus.INTERNAL_SERVER_ERROR));

    final Optional<JsonDataResponse> response = this.client.find(1l);

    assertTrue(response.isEmpty());
    verify(this.restTemplate, times(1)).exchange(DATA_SERVICE_URI, HttpMethod.GET, requestEntity, JsonDataResponse.class, 1L);

    MDC.clear();
  }
}