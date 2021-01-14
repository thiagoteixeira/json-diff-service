package com.thiagoteixeira.jsondiffbusiness.interceptor;

import com.thiagoteixeira.jsondiffbusiness.util.ApiHeaders;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * A request intercept instance to get the @{@link ApiHeaders#TRACE_ID_HEADER} from request and put it
 * into {@link MDC}, to make traceability easy.
 *
 * @author thiagoteixeira
 */
@Component
public class RequestInterceptor implements HandlerInterceptor {
  private static final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);

  /**
   * It is responsible to get the {@link ApiHeaders#TRACE_ID_HEADER} header from the current {@link HttpServletRequest}
   * instance and put it in the {@link MDC}. It will be used later in the log4j2 logs, it will be available as a custom
   * variable into all loggers.
   *
   * @param request The current {@link HttpServletRequest}  instance
   * @param response The current {@link HttpServletResponse}  instance
   * @param handler The handler instance
   * @return Return {@code true} then the interception was successful, otherwise {@code false}   *
   */
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    logger.info("Putting traceId header into MDC");
    final var value = request.getHeader(ApiHeaders.TRACE_ID_HEADER);
    MDC.put(ApiHeaders.TRACE_ID_HEADER, value);
    return true;
  }

  /**
   * It is responsible o execute the {@link MDC#clear()} method.
   * @see MDC#clear()
   *
   * @param request The current {@link HttpServletRequest}  instance
   * @param response The current {@link HttpServletResponse}  instance
   * @param handler The handler instance
   * @param ex Any exception thrown on handler execution
   */
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    MDC.clear();
  }
}
