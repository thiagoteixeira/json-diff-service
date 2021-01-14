package com.thiagoteixeira.jsondiffbusiness.service;

import com.thiagoteixeira.jsondiffbusiness.vo.Result;
import java.util.Optional;

/**
 * Service implementation that provide methods to allow {@link com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse} entity sides comparison.
 *
 * @author thiagoteixeira
 */
public interface DiffService {

  /**
   * Compares the bytes of the sides of a {@link com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse} object instance.
   *
   * @param id The {@link com.thiagoteixeira.jsondiffbusiness.remote.entity.JsonDataResponse} unique identifier
   *
   * @return The result of the comparison represented by an {@link Result} instance encapsulated into an {@link Optional} instance.
   */
  Optional<Result> compareJsonSides(long id);
}
