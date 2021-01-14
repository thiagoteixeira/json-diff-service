package com.thiagoteixeira.jsondiffdata.resource;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonSide;
import com.thiagoteixeira.jsondiffdata.interceptor.RequestInterceptor;
import com.thiagoteixeira.jsondiffdata.repository.JsonRepository;
import com.thiagoteixeira.jsondiffdata.util.ApiHeaders;
import com.thiagoteixeira.jsondiffdata.vo.JsonRequest;
import java.util.Optional;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Integration tests for {@link JsonResourceImpl} features.
 *
 * @author thiagoteixeira
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
class JsonResourceImplIntegrationTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JsonRepository respository;

  @MockBean
  private RequestInterceptor interceptor;

  @BeforeEach
  void setUp() throws Exception {
    given(this.interceptor.preHandle(any(),any(),any())).willReturn(true);
  }

  @Test
  void saveLeftSideSuccessfully() throws Exception {
    // given
    final JsonEntity expected = new JsonEntity(1L, "foo", "bar");
    given(this.respository.findById(1L)).willReturn(Optional.of(expected));
    given(this.respository.save(expected)).willReturn(expected);

    final var request = new JsonRequest();
    request.setValue("foo");

    // when/then
    this.mvc.perform(
        post("/v1/diff/{id}/left", 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(ApiHeaders.TRACE_ID_HEADER, "123")
            .content(this.objectMapper.writeValueAsBytes(request)))
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expected)));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.respository, times(1)).save(eq(expected));
  }

  @Test
  void saveRightSideSuccessfully() throws Exception {
    // given
    final JsonEntity expected = new JsonEntity(1L, "foo", "bar");
    given(this.respository.findById(1L)).willReturn(Optional.of(expected));
    given(this.respository.save(expected)).willReturn(expected);

    final var request = new JsonRequest();
    request.setValue("bar");

    // when/then
    this.mvc.perform(
        post("/v1/diff/{id}/right", 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(ApiHeaders.TRACE_ID_HEADER, "123")
            .content(this.objectMapper.writeValueAsBytes(request)))
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expected)));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.respository, times(1)).save(eq(expected));
  }

  @Test
  void findSuccessfully() throws Exception {
    // given
    final JsonEntity expected = new JsonEntity(1L, "right", "left");
    given(this.respository.findById(1L)).willReturn(Optional.of(expected));

    // when/then
    this.mvc.perform(
        get("/v1/diff/{id}", 1L)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .header(ApiHeaders.TRACE_ID_HEADER, "123"))
        .andExpect(status().is(HttpStatus.OK.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expected)));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.respository, times(1)).findById(1L);
  }

  @Test
  void findNotFound() throws Exception {
    // given
    given(this.respository.findById(1L)).willReturn(Optional.empty());

    // when/then
    this.mvc.perform(
        get("/v1/diff/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(ApiHeaders.TRACE_ID_HEADER, "123"))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        .andExpect(content().string(Strings.EMPTY));

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.respository, times(1)).findById(1L);
  }
}