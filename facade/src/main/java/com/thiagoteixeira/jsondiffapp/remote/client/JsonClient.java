package com.thiagoteixeira.jsondiffapp.remote.client;

import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import java.util.Optional;
import org.springframework.http.ResponseEntity;

/**
 * A HTTP client to create/retrieve information to/from json-diff-data microservice.
 *
 * @author thiagoteixeira
 */
public interface JsonClient {

  /**
   * It allows us to receive a JSON Base64 entity via HTTP request to be record later into some
   *  {@link DataResponse} instances via json-diff-data microservice.
   * @param id The {@link DataResponse} unique identifier.
   * @param request The {@link DataRequest} have the side and value that will be record in the database.
   * @return The persisted {@link DataResponse} instance encapsulated into a {@link ResponseEntity}
   */
  Optional<DataResponse> save(final long id, final JsonSide side, final DataRequest request);

  /**
   * It allow us to retrieve the bytes comparison given a JSON entity via json-diff-business microservice.
   * @param id The {@link DataResponse} unique identifier.
   * @return The result of the comparison represented by an {@link BusinessResponse} instance encapsulated into {@link ResponseEntity} instance.
   */
  Optional<BusinessResponse> diff(final long id);

}
