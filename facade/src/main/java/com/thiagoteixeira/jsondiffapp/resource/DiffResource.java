package com.thiagoteixeira.jsondiffapp.resource;

import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.vo.DiffRequest;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * A {@link org.springframework.web.bind.annotation.RestController} instance to allow send and retrieve
 * some JSON entity features.
 *
 * @author thiagoteixeira
 */
@RequestMapping("/v1/diff/{id:[0-9]+}")
public interface DiffResource {

    /**
     * Allows to save a JSON Base64 value into the left side given a JSON entity ID.
     * In the end this will create or update the JSON entity.
     *
     * @param id The JSON entity unique identifier
     * @param request The request body object represented by {@link DiffRequest}
     * @return The persisted {@link DataResponse} instance encapsulated into a {@link ResponseEntity}
     */
    @PostMapping(value = "/left", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<DataResponse> saveLeft(@PathVariable Long id, @RequestBody DiffRequest request);

    /**
     * Allows to save a JSON Base64 value into the right side given a JSON entity ID.
     * In the end this will create or update the JSON entity.
     *
     * @param id The JSON entity unique identifier
     * @param request The request body object represented by {@link DiffRequest}
     * @return The persisted {@link DataResponse} instance encapsulated into a {@link ResponseEntity}
     */
    @PostMapping(value = "/right", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<DataResponse> saveRight(@PathVariable Long id, @RequestBody DiffRequest request);

    /**
     * Allows to compare the bytes given a JSON entity via json-diff-business microservice.
     * @param id The JSON entity unique identifier
     * @return The result of the comparison represented by an {@link DiffResponse} instance encapsulated into {@link ResponseEntity} instance.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<DiffResponse> getDiff(@PathVariable Long id);
}
