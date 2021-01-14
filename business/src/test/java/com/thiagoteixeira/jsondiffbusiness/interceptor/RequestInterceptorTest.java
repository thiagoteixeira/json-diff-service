package com.thiagoteixeira.jsondiffbusiness.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.thiagoteixeira.jsondiffbusiness.util.ApiHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Unit tests for {@link RequestInterceptor}
 *
 * @author thiagoteixeira
 */
class RequestInterceptorTest {

  @BeforeEach
  void setUp() {
    MDC.clear();
  }

  @Test
  void preHandleWithRequestTraceIdHeader() throws Exception {
    final var expectedTraceIdHeader = "11111";
    final var request = new MockHttpServletRequest();
    request.addHeader(ApiHeaders.TRACE_ID_HEADER, expectedTraceIdHeader);

    assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
    new RequestInterceptor().preHandle(request, null, null);
    assertEquals(expectedTraceIdHeader, MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }

  @Test
  void preHandleWithoutRequestTraceIdHeader() throws Exception {
    assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
    new RequestInterceptor().preHandle(new MockHttpServletRequest(), null, null);
    assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }

  @Test
  void afterCompletion() throws Exception {
    MDC.put(ApiHeaders.TRACE_ID_HEADER, "111");
    new RequestInterceptor().afterCompletion(null, null, null, null);
    assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }
}