package com.thiagoteixeira.jsondiffapp.resource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.interceptor.RequestInterceptor;
import com.thiagoteixeira.jsondiffapp.remote.client.JsonClient;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.util.ApiHeaders;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.nio.charset.StandardCharsets;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * Integration tests for {@link DiffResourceImpl} features.
 *
 * @author thiagoteixeira
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class DiffResourceImplTest {

  private static final String DIFF_API_PATH = "/v1/diff/{id}";

  private static final String SAVE_LEFT_API_PATH = "/v1/diff/{id}/left";

  private static final String SAVE_RIGHT_API_PATH = "/v1/diff/{id}/right";

  private static final String BUSINESS_EQUAL_CONTENT_CODE = "equal_content";

  private static final String BUSINESS_NOT_EQUAL_CODE = "not_equal";

  private static final String BUSINESS_EQUAL_SIZE_DIFF_CONTENT_CODE = "equal_size_different_content";

  @Autowired
  private MockMvc mvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private JsonClient client;

  @MockBean
  private RequestInterceptor interceptor;

  @BeforeEach
  void setUp() throws Exception {
    given(this.interceptor.preHandle(any(),any(),any())).willReturn(true);
  }

  @Test
  void saveLeftSuccessful() throws Exception {
    // given
    final var expectedResponse = new DataResponse();
    expectedResponse.setId(1L);
    expectedResponse.setLeft("foo");
    expectedResponse.setRight(null);

    final var dto = new DataRequest("foo");
    given(this.client.save(1L, JsonSide.LEFT, dto)).willReturn(Optional.of(expectedResponse));

    // when/then
    final MvcResult result = this.mvc.perform(
        post(SAVE_LEFT_API_PATH, 1L)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString())
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(this.objectMapper.writeValueAsString(new DataRequest("foo")))
    )
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expectedResponse)))
        .andReturn();

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).save(1L, JsonSide.LEFT, dto);
  }

  @Test
  void saveRightSuccessful() throws Exception {
    // given
    final var expectedResponse = new DataResponse();
    expectedResponse.setId(1L);
    expectedResponse.setLeft(null);
    expectedResponse.setRight("bar");

    final var dto = new DataRequest("bar");
    given(this.client.save(1L, JsonSide.RIGHT,dto)).willReturn(Optional.of(expectedResponse));

    // when/then
    final MvcResult result = this.mvc.perform(
        post(SAVE_RIGHT_API_PATH, 1L)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(this.objectMapper.writeValueAsString(new DataRequest("bar")))
    )
        .andExpect(status().is(HttpStatus.CREATED.value()))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().string(this.objectMapper.writeValueAsString(expectedResponse)))
        .andReturn();

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).save(1L, JsonSide.RIGHT, dto);
  }

  @Test
  void getDiffEqualContentWithDefaultLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_CONTENT_CODE);
    getDiff(tmp, Strings.EMPTY, new DiffResponse("The JSON contents are equal!"));
  }

  @Test
  void getDiffSuccessfullyWithEnglishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_CONTENT_CODE);
    getDiff(tmp,"en", new DiffResponse("The JSON contents are equal!"));
  }

  @Test
  void getDiffSuccessfullyWithPortugueseLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_CONTENT_CODE);
    getDiff(tmp,"pt", new DiffResponse("Os conteúdos JSON são iguais!"));
  }

  @Test
  void getDiffSuccessfullyWithSpanishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_CONTENT_CODE);
    getDiff(tmp,"es", new DiffResponse("¡Los contenidos JSON son iguales!"));
  }

  @Test
  void getDiffNotEqualContentWithDefaultLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_NOT_EQUAL_CODE);
    getDiff(tmp, Strings.EMPTY, new DiffResponse("The JSON contents have not the same size!"));
  }

  @Test
  void getDiffNotEqualContentWithEnglishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_NOT_EQUAL_CODE);
    getDiff(tmp, "en", new DiffResponse("The JSON contents have not the same size!"));
  }

  @Test
  void getDiffNotEqualContentWithPortugueseLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_NOT_EQUAL_CODE);
    getDiff(tmp, "pt", new DiffResponse("Os conteúdos JSON não possuem o mesmo tamanho!"));
  }

  @Test
  void getDiffNotEqualContentWithSpanishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_NOT_EQUAL_CODE);
    getDiff(tmp, "es", new DiffResponse("¡Los contenidos JSON no tienen el mismo tamaño!"));
  }

  @Test
  void getDiffForEqualSizeAndDifferentContentWithDefaultLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_SIZE_DIFF_CONTENT_CODE, "0,2");
    getDiff(tmp, Strings.EMPTY, new DiffResponse("The JSON contents have the same size, but offsets are different: 0,2"));
  }

  @Test
  void getDiffForEqualSizeAndDifferentContentWithEnglishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_SIZE_DIFF_CONTENT_CODE, "0,2");
    getDiff(tmp, "en", new DiffResponse("The JSON contents have the same size, but offsets are different: 0,2"));
  }

  @Test
  void getDiffForEqualSizeAndDifferentContentWithPortugueseLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_SIZE_DIFF_CONTENT_CODE, "0,2");
    getDiff(tmp, "pt", new DiffResponse("Os conteúdos JSON possuem o mesmo tamanho, mas os deslocamentos são diferentes: 0,2"));
  }

  @Test
  void getDiffForEqualSizeAndDifferentContentWithSpanishLanguage() throws Exception {
    var tmp = mockBusinessResponse(BUSINESS_EQUAL_SIZE_DIFF_CONTENT_CODE, "0,2");
    getDiff(tmp, "es", new DiffResponse("El contenidos JSON tiene el mismo tamaño, pero las compensaciones son diferentes: 0,2"));
  }

  @Test
  void getDiffNotFound() throws Exception {
    // given
    given(this.client.diff(1L)).willReturn(Optional.empty());

    // when/then
    final MvcResult result = this.mvc.perform(
        get(DIFF_API_PATH, 1L)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString())
    )
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
        .andExpect(content().string(Strings.EMPTY))
        .andReturn();

    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).diff(1L);
  }

  private void getDiff(final BusinessResponse businessResponse, final String acceptedLanguage, final DiffResponse expectedMessage) throws Exception {
    // given
    given(this.client.diff(1L)).willReturn(Optional.of(businessResponse));

    // when/then
    final MvcResult result = this.mvc.perform(
        get(DIFF_API_PATH, 1L)
            .header(ApiHeaders.TRACE_ID_HEADER, UUID.randomUUID().toString())
            .header(HttpHeaders.ACCEPT_LANGUAGE, acceptedLanguage)
    )
    .andExpect(status().is(HttpStatus.OK.value()))
    .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
    .andReturn();

    assertEquals(this.objectMapper.writeValueAsString(expectedMessage), result.getResponse().getContentAsString(
        StandardCharsets.UTF_8));
    verify(this.interceptor, times(1)).preHandle(any(),any(),any());
    verify(this.interceptor, times(1)).afterCompletion(any(),any(),any(),any());
    verify(this.client, times(1)).diff(1L);
  }

  private BusinessResponse mockBusinessResponse(final String code) {
    return mockBusinessResponse(code, null);
  }

  private BusinessResponse mockBusinessResponse(final String code, final String arguments) {
    var response = new BusinessResponse();
    response.setCode(code);
    response.setMessage("foo");
    response.setArguments(arguments);
    return response;
  }
}