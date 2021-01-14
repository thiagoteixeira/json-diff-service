package com.thiagoteixeira.jsondiffbusiness.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagoteixeira.jsondiffbusiness.interceptor.RequestInterceptor;
import com.thiagoteixeira.jsondiffbusiness.remote.client.JsonDataClient;
import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import com.thiagoteixeira.jsondiffbusiness.util.ApiHeaders;
import com.thiagoteixeira.jsondiffbusiness.vo.Result;
import com.thiagoteixeira.jsondiffbusiness.vo.Result.Type;
import java.util.Optional;
import java.util.UUID;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link DiffResourceImpl} features.
 *
 * @author thiagoteixeira
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DiffResourceImplIntegrationTest {

  private static final String DIFF_API_PATH = "/v1/diff/{id}";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JsonDataClient client;

  @MockBean
  private RequestInterceptor interceptor;

  @BeforeEach
  void setUp() throws Exception {
    given(this.interceptor.preHandle(any(),any(),any())).willReturn(true);
  }

  @Test
  void getDiffSuccessfully() throws Exception {
    // given
    final Result expected = new Result(Type.EQUAL_CONTENT);
    given(this.client.find(1L)).willReturn(Optional.of(new JsonDataResponse(1L, "foo", "foo")));

    // when/then
    this.mvc.perform(
        get(DIFF_API_PATH, 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString()))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expected)));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).find(1L);
  }

  @Test
  void getDiffNotFound() throws Exception {
    // given
    given(this.client.find(1L)).willReturn(Optional.empty());

    // when/then
    this.mvc.perform(
        get(DIFF_API_PATH, 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString()))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        .andExpect(content().string(Strings.EMPTY));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).find(1L);
  }
}