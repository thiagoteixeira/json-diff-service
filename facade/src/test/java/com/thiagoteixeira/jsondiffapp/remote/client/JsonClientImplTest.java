package com.thiagoteixeira.jsondiffapp.remote.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.util.ApiHeaders;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Unit tests for {@link JsonClientImpl}
 *
 * @author thiagoteixeira
 */
@ExtendWith(MockitoExtension.class)
class JsonClientImplTest {

  private static final String BUSINESS_SERVICE_URI = "http://127.0.0.1:8081/diff/{id}";

  private static final String DATA_SERVICE_URI = "http://127.0.0.1:8082/data/{id}";

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private JsonClientImpl client;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(this.client, "businessUri", BUSINESS_SERVICE_URI);
    ReflectionTestUtils.setField(this.client, "dataUri", DATA_SERVICE_URI);
  }

  @Test
  void createLeftSuccessfully() {
    final DataResponse expected = new DataResponse();
    expected.setId(1L);
    expected.setLeft("foo");
    expected.setLeft("bar");

    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    final var request = new DataRequest("foo");
    final var requestEntity = new HttpEntity<>(request, headers);
    final String uri = this.client.buildSaveUri(JsonSide.LEFT, 1L);
    when(this.restTemplate.exchange(uri, HttpMethod.POST, requestEntity, DataResponse.class))
        .thenReturn(ResponseEntity.ok(expected));

    final Optional<DataResponse> response = this.client.save(1L, JsonSide.LEFT, request);

    verify(this.restTemplate, times(1))
        .exchange(uri, HttpMethod.POST, requestEntity, DataResponse.class);
    assertTrue(response.isPresent());
    assertSame(expected, response.get());
  }

  @Test
  void diffSuccessfully() {
    var expected = new BusinessResponse();
    expected.setCode("foo");
    expected.setMessage("bar");
    expected.setArguments("baz");

    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L))
        .thenReturn(
            ResponseEntity.ok(expected));

    final Optional<BusinessResponse> response = this.client.diff(1L);

    verify(this.restTemplate, times(1))
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L);
    assertTrue(response.isPresent());
    assertSame(expected, response.get());
  }

  @Test
  void diffNotFoundFromBusinessNotFoundResponse() {
    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L))
        .thenThrow(new HttpClientErrorException(
            HttpStatus.NOT_FOUND));

    Optional<BusinessResponse> response = this.client.diff(1L);

    verify(this.restTemplate, times(1))
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L);
    assertTrue(response.isEmpty());
  }

  @Test
  void diffNotFoundBusinessInternalServerErrorResponse() {
    final var traceIdHeader = UUID.randomUUID().toString();
    MDC.put(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);

    final var headers = new HttpHeaders();
    headers.set(ApiHeaders.TRACE_ID_HEADER, traceIdHeader);
    headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    final var requestEntity = new HttpEntity<>(null, headers);
    when(this.restTemplate
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L))
        .thenThrow(new HttpClientErrorException(
            HttpStatus.INTERNAL_SERVER_ERROR));

    Optional<BusinessResponse> response = this.client.diff(1L);

    verify(this.restTemplate, times(1))
        .exchange(BUSINESS_SERVICE_URI, HttpMethod.GET, requestEntity, BusinessResponse.class, 1L);
    assertTrue(response.isEmpty());
  }

  @Test
  void buildSaveUriSuccessful() {
    assertEquals("http://127.0.0.1:8082/data/1/left", this.client.buildSaveUri(JsonSide.LEFT, 1L));
    assertEquals("http://127.0.0.1:8082/data/1/right",
        this.client.buildSaveUri(JsonSide.RIGHT, 1L));
  }

  @Test
  void buildSaveUriMissingArgument() {
    assertThrows(IllegalArgumentException.class, () -> this.client.buildSaveUri(JsonSide.RIGHT));
    assertThrows(IllegalArgumentException.class, () -> this.client.buildSaveUri(JsonSide.RIGHT, null));
    assertThrows(IllegalArgumentException.class, () -> this.client.buildSaveUri(JsonSide.RIGHT, new String[0]));
  }
}