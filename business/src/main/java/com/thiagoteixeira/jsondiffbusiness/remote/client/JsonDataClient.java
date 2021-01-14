package com.thiagoteixeira.jsondiffbusiness.remote.client;

import com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse;
import java.util.Optional;

/**
 * A HTTP client to retrieve information from json-diff-data microservice.
 *
 * @author thiagoteixeira
 */
public interface JsonDataClient {

  /**
   * Retrieves a {@link JsonDataResponse} entity from the json-diff-data microservice.
   *
   * @param id The {@link JsonDataResponse} unique identifier.
   * @return if found then returns the persisted {@link JsonDataResponse} instance encapsulated
   * into an {@link Optional} instance, otherwise an empty {@link Optional}.
   */
  public Optional<JsonDataResponse> find(final long id);
}
