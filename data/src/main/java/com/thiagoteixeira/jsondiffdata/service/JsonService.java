package com.thiagoteixeira.jsondiffdata.service;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonDto;
import java.util.Optional;

/**
 * Service that provides methods to write and read {@link JsonEntity} via {@link com.thiagoteixeira.jsondiffdata.repository.JsonRepository}
 *
 * @author thiagoteixeira
 */
public interface JsonService {

    /**
     * Insert or update a received {@link JsonDto} information received into a {@link JsonEntity} representation.
     *
     * @param data A {@link JsonDto} information
     * @return The persited {@link JsonEntity} instance
     */
    JsonEntity save(final JsonDto data);

    /**
     * Retrieves a {@link JsonEntity} via {@link com.thiagoteixeira.jsondiffdata.repository.JsonRepository}
     * based on the unique identifier received.
     * @param id The {@link JsonEntity} unique identifier
     * @return if found then returns the persisted {@link JsonEntity} instance encapsulated into an
     *      * {@link Optional} instance, otherwise an emtpty {@link Optional} instance.
     */
    Optional<JsonEntity> find(final Long id);
}
