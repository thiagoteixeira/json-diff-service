package com.thiagoteixeira.jsondiffapp.service;

import com.thiagoteixeira.jsondiffapp.dto.JsonDto;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 * Service implementation that provide methods to allow {@link DataResponse} entity upsert or side comparison.
 *
 * @author thiagoteixeira
 */
public interface DiffService {

  /**
   * It allows us to save a JSON Base64 entity via HTTP request to json-diff-data microservice.
   * The data microservice executes an upsert(create/update) operation.
   *
   * @param data The {@link JsonDto} representing the received data information.
   * @return The persisted {@link DataResponse} instance encapsulated into an {@link Optional} instance.
   */
  Optional<DataResponse> save(final JsonDto data);

  /**
   * Compares the bytes given a JSON entity via json-diff-business microservice.
   * After comparison, execute i18n translating the message based on the {@link BusinessResponse#getCode()} field
   * into the language received in the header {@link org.springframework.http.HttpHeaders#ACCEPT_LANGUAGE}. If there is no translate configuration
   * for the received language, the default message will be in English.
   *
   * @param id The {@link DataResponse} unique identifier.
   * @return The result of the comparison represented by an {@link DiffResponse} instance encapsulated into {@link ResponseEntity} instance.
   */
  Optional<DiffResponse> diff(final long id);
}
