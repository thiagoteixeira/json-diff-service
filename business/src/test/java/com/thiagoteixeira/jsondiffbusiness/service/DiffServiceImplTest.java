package com.thiagoteixeira.jsondiffbusiness.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.thiagoteixeira.jsondiffbusiness.remote.client.JsonDataClientImpl;
import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import com.thiagoteixeira.jsondiffbusiness.vo.Result.Type;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link DiffServiceImpl}
 *
 * @author thiagoteixeira
 */
@ExtendWith(MockitoExtension.class)
class DiffServiceImplTest {

  @Mock
  private JsonDataClientImpl client;

  @InjectMocks
  private DiffServiceImpl service;

  @Test
  void compareJsonSidesNotFound() {
    when(this.client.find(1L)).thenReturn(Optional.empty());

    final var result = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  void compareJsonSidesWithoutRight() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft("foo");
    mockedDataResponse.setRight(null);

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getCode(), result.getCode());
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getDescription(), result.getMessage());
  }

  @Test
  void compareJsonSidesWithoutLeft() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft(null);
    mockedDataResponse.setRight("foo");

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getCode(), result.getCode());
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getDescription(), result.getMessage());
  }

  @Test
  void compareJsonSidesWithoutBothSides() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft(null);
    mockedDataResponse.setRight(null);

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.EQUAL_CONTENT.getCode(), result.getCode());
    Assertions.assertEquals(Type.EQUAL_CONTENT.getDescription(), result.getMessage());
  }

  @Test
  void compareJsonSidesWithEqualSides() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft("xpto");
    mockedDataResponse.setRight("xpto");

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.EQUAL_CONTENT.getCode(), result.getCode());
    Assertions.assertEquals(Type.EQUAL_CONTENT.getDescription(), result.getMessage());
  }

  @Test
  void compareJsonSidesWithDifferentSizes() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft("barbaz");
    mockedDataResponse.setRight("foo");

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getCode(), result.getCode());
    Assertions.assertEquals(Type.NOT_EQUAL_SIZE.getDescription(), result.getMessage());
  }

  @Test
  void compareJsonSidesWithDifferentContentAndEqualSize() {
    var mockedDataResponse = new JsonDataResponse();
    mockedDataResponse.setId(1L);
    mockedDataResponse.setLeft("bar");
    mockedDataResponse.setRight("for");

    when(this.client.find(1L)).thenReturn(Optional.of(mockedDataResponse));

    final var resultOpt = this.service.compareJsonSides(1L);

    verify(this.client, times(1)).find(1L);
    Assertions.assertTrue(resultOpt.isPresent());
    final var result = resultOpt.get();
    Assertions.assertEquals(Type.EQUAL_SIZE_DIFFERENT_CONTENT.getCode(), result.getCode());
    Assertions.assertEquals("0, 1", result.getArguments());
    Assertions.assertEquals(String.format(Type.EQUAL_SIZE_DIFFERENT_CONTENT.getDescription(), "0, 1"), result.getMessage());
  }
}