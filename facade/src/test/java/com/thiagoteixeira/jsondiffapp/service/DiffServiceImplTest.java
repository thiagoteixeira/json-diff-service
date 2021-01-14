package com.thiagoteixeira.jsondiffapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thiagoteixeira.jsondiffapp.dto.JsonDto;
import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.remote.client.JsonClientImpl;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.util.Locale;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

/**
 * Unit tests for {@link DiffServiceImpl}
 *
 * @author thiagoteixeira
 */
@ExtendWith(MockitoExtension.class)
class DiffServiceImplTest {

  @Mock
  private JsonClientImpl client;

  @Mock
  private MessageSource messageSource;

  @InjectMocks
  private DiffServiceImpl service;

  @Test
  void createSuccessfully() {
    final var expected = new DataResponse();
    when(this.client.create(1L, new DataRequest(JsonSide.LEFT, "foo"))).thenReturn(Optional.of(expected));

    final var dto = JsonDto.builder()
        .withId(1L)
        .withSide(JsonSide.LEFT)
        .withValue("foo")
        .build();

    final Optional<DataResponse> result = this.service.save(dto);
    assertTrue(result.isPresent());
    assertSame(expected, result.get());
  }

  @Test
  void diffNotFound() {
    when(this.client.diff(1l)).thenReturn(Optional.empty());

    Optional<DiffResponse> result = this.service.diff(1L);

    verify(this.client, times(1)).diff(1L);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void diffSuccessfully() {
    final var businessResponse = new BusinessResponse();
    businessResponse.setCode("equal_content");
    businessResponse.setMessage("Xpto");

    when(this.client.diff(1l)).thenReturn(Optional.of(businessResponse));
    when(this.messageSource.getMessage(eq(businessResponse.getCode()), any(), any(Locale.class))).thenReturn("The JSON contents are equal!");

    Optional<DiffResponse> result = this.service.diff(1L);

    verify(this.client, times(1)).diff(1L);
    Assertions.assertTrue(result.isPresent());
    final var data = result.get();
    assertEquals("The JSON contents are equal!", data.getMessage());
  }
}