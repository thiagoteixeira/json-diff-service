package com.thiagoteixeira.jsondiffdata.resource;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.vo.JsonRequest;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link org.springframework.web.bind.annotation.RestController} instance to allow receive and find
 * some {@link JsonEntity} information.
 *
 * @author thiagoteixeira
 */
@RequestMapping("/v1/jsondata/{id:[0-9]+}")
public interface JsonResource {

    /**
     * It allows us to receive a JSON Base64 entity via HTTP request to be record later into some
     * {@link JsonEntity} instances.
     * This method executes a upsert operation, in other words, insert and update.
     *
     * @param id The {@link JsonEntity} unique identifier.
     * @param body  The {@link JsonRequest} have the side and value that will be record in the database.
     * @return The persisted {@link JsonEntity} instance encapsulated into a {@link ResponseEntity}
     */
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<JsonEntity> save(@PathVariable Long id, @Valid @RequestBody JsonRequest body);

    /**
     * It allows us to retrieve some {@link JsonEntity} instance base on the unique identifier.
     *
     * @param id The {@link JsonEntity} unique identifier.
     * @return if found then returns the persisted {@link JsonEntity} instance encapsulated into a
     * {@link ResponseEntity} instance, otherwise a {@link ResponseEntity} without body.
     */
    @GetMapping(
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<JsonEntity> find(@PathVariable Long id);
}
