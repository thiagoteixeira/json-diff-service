package com.thiagoteixeira.jsondiffdata.service;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mongodb.MongoException;
import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonDto;
import com.thiagoteixeira.jsondiffdata.dto.JsonSide;
import com.thiagoteixeira.jsondiffdata.repository.JsonRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Unit tests for {@link JsonServiceImpl}
 *
 * @author thiagoteixeira
 */
@ExtendWith(MockitoExtension.class)
class JsonServiceImplTest {

  @Mock
  private JsonRepository repository;

  @InjectMocks
  private JsonServiceImpl service;

  @Test
  void saveSuccessfulForExistentEntity() {
    // given
    var input = JsonDto
                    .builder()
                    .withId(1L)
                    .withSide(JsonSide.LEFT)
                    .withValue("left")
                    .build();

    var databaseEntity = Optional.of(new JsonEntity(1L, "xpto", "right"));
    when(this.repository.findById(1L)).thenReturn(databaseEntity);
    var expectedResult = new JsonEntity(input.getId(), input.getValue(), "right");
    var entityToBeSaved = new JsonEntity(input.getId(), input.getValue(), "right");
    when(this.repository.save(eq(entityToBeSaved))).thenReturn(expectedResult);

    // when
    final JsonEntity result = this.service.save(input);

    // then
    verify(this.repository, times(1)).findById(1L);
    verify(this.repository, times(1)).save(eq(entityToBeSaved));
    assertSame(expectedResult, result);
  }

  @Test
  void saveSuccessfulForNonExistentEntity() {
    // given
    var input = JsonDto
        .builder()
        .withId(1L)
        .withSide(JsonSide.LEFT)
        .withValue("left")
        .build();

    when(this.repository.findById(1L)).thenReturn(Optional.empty());
    var expectedResult = new JsonEntity(input.getId(), input.getValue(), "right");
    var entityToBeSaved = new JsonEntity(input.getId(), input.getValue(), null);
    when(this.repository.save(eq(entityToBeSaved))).thenReturn(expectedResult);

    // when
    final JsonEntity result = this.service.save(input);

    // then
    verify(this.repository, times(1)).findById(1L);
    verify(this.repository, times(1)).save(eq(entityToBeSaved));
    assertSame(expectedResult, result);
  }

  @Test
  void saveUnsuccessfullyByRepositoryFindByIdError() {
    // given
    var input = JsonDto
        .builder()
        .withId(1L)
        .withSide(JsonSide.LEFT)
        .withValue("left")
        .build();

    when(this.repository.findById(1L)).thenThrow(new MongoException("error"));

    // when/then
    assertThrows(MongoException.class, () -> this.service.save(input));
    verify(this.repository, times(1)).findById(1L);
    verify(this.repository, times(0)).save(any(JsonEntity.class));
  }

  @Test
  void saveUnsuccessfullyByRepositorySaveError() {
    // given
    var input = JsonDto
        .builder()
        .withId(1L)
        .withSide(JsonSide.LEFT)
        .withValue("left")
        .build();

    when(this.repository.findById(1L)).thenReturn(Optional.empty());

    var entityToBeSaved = new JsonEntity(input.getId(), input.getValue(), null);
    when(this.repository.save(eq(entityToBeSaved))).thenThrow(new MongoException("error"));

    // when/then
    assertThrows(MongoException.class, () -> this.service.save(input));
    verify(this.repository, times(1)).findById(1L);
    verify(this.repository, times(1)).save(eq(entityToBeSaved));
  }

  @Test
  void findSuccessfully() {
    // given
    var expectedResult = Optional.of(new JsonEntity());
    when(this.repository.findById(1L)).thenReturn(expectedResult);

    // when
    Optional<JsonEntity> result = this.service.find(1L);

    // then
    verify(this.repository, times(1)).findById(1L);
    assertSame(expectedResult, result);
  }

  @Test
  void findUnsuccessfully() {
    // given
    when(this.repository.findById(1L)).thenThrow(new MongoException("error"));

    // when/then
    assertThrows(MongoException.class, () -> this.service.find(1L));
    verify(this.repository, times(1)).findById(1L);
  }

}