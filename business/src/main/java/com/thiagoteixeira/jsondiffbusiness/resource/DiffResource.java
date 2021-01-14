package com.thiagoteixeira.jsondiffbusiness.resource;

import com.thiagoteixeira.jsondiffbusiness.vo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link org.springframework.web.bind.annotation.RestController} instance to allow compare the sides from
 * some JSON entity instances.
 *
 * @author thiagoteixeira
 */

public interface DiffResource {

  /**
   * Compares the bytes given a JSON entity via {@link com.thiagoteixeira.jsondiffbusiness.service.DiffService#compareJsonSides(long)}.
   *
   * @param id The {@link com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse} unique identifier.
   * @return The result of the comparison represented by an {@link Result} instance encapsulated into {@link ResponseEntity} instance.
   */
  @GetMapping(
      value = "/v1/diff/{id:[0-9]+}",
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  @ResponseStatus(HttpStatus.OK)
  ResponseEntity<Result> getDiff(@PathVariable Long id);
}
