package com.thiagoteixeira.jsondiffbusiness.config;
import com.thiagoteixeira.jsondiffbusiness.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * A @{@link WebMvcConfigurer} implementation that allow os to install the @{@link RequestInterceptor} instance.
 *
 * @author thiagoteixeira
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  private final RequestInterceptor requestInterceptor;

  @Autowired
  public MvcConfig(final RequestInterceptor requestInterceptor) {
    this.requestInterceptor = requestInterceptor;
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(this.requestInterceptor);
  }
}