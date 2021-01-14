package com.thiagoteixeira.jsondiffapp.interceptor;

import com.thiagoteixeira.jsondiffapp.util.ApiHeaders;
import org.junit.jupiter.api.Assertions;
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

    Assertions.assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
    new RequestInterceptor().preHandle(request, null, null);
    Assertions.assertEquals(expectedTraceIdHeader, MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }

  @Test
  void preHandleWithoutRequestTraceIdHeader() throws Exception {
    Assertions.assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
    new RequestInterceptor().preHandle(new MockHttpServletRequest(), null, null);
    Assertions.assertNotNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }

  @Test
  void afterCompletion() throws Exception {
    MDC.put(ApiHeaders.TRACE_ID_HEADER, "111");
    new RequestInterceptor().afterCompletion(null, null, null, null);
    Assertions.assertNull(MDC.get(ApiHeaders.TRACE_ID_HEADER));
  }
}