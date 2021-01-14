package com.thiagoteixeira.jsondiffdata.resource;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonDto;
import com.thiagoteixeira.jsondiffdata.dto.JsonSide;
import com.thiagoteixeira.jsondiffdata.service.JsonService;
import com.thiagoteixeira.jsondiffdata.vo.JsonRequest;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@inheritDoc}
 *
 * @see JsonResource
 * @author thiagoteixeira
 */
@RestController
public class JsonResourceImpl implements JsonResource {

    private static final Logger logger = LoggerFactory.getLogger(JsonResourceImpl.class);

    private final JsonService service;

    @Autowired
    public JsonResourceImpl(final JsonService service) {
        this.service = service;
    }

    /**
     * {@inheritDoc}
     *
     * @see JsonResource#saveLeft(Long, JsonRequest)
     */
    @Override
    public ResponseEntity<JsonEntity> saveLeft(final Long id, final JsonRequest body) {
        logger.info("Receiving the left side value '{}' for JSON id '{}'", body, id);
        final var dto = JsonDto
            .builder()
            .withId(id)
            .withSide(JsonSide.LEFT)
            .withValue(body.getValue())
            .build();
        final JsonEntity result = this.service.save(dto);
        logger.info("The left side value has been saved into JSON id '{}'", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * {@inheritDoc}
     *
     * @see JsonResource#saveRight(Long, JsonRequest)
     */
    @Override
    public ResponseEntity<JsonEntity> saveRight(final Long id, final JsonRequest body) {
        logger.info("Receiving the right side value '{}' for JSON id '{}'", body, id);
        final var dto = JsonDto
            .builder()
            .withId(id)
            .withSide(JsonSide.RIGHT)
            .withValue(body.getValue())
            .build();
        final JsonEntity result = this.service.save(dto);
        logger.info("The right side value has been saved into JSON id '{}'", id);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    /**
     * {@inheritDoc}
     *
     * @see JsonResource#find(Long)
     */
    @Override
    public ResponseEntity<JsonEntity> find(final Long id) {
        logger.info("Retrieving JSON id '{}'", id);
        final Optional<JsonEntity> data = this.service.find(id);
        if(data.isPresent()){
            logger.info("JSON id '{}' found.", id);
            return ResponseEntity.ok(data.get());
        }
        logger.info("JSON id '{}' not found.", id);
        return ResponseEntity.notFound().build();
    }

}
