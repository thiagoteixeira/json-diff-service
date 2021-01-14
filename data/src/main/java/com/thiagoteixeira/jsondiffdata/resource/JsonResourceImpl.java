package com.thiagoteixeira.jsondiffdata.resource;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonDto;
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
     * @see JsonResource#save(Long, JsonRequest)
     */
    @Override
    public ResponseEntity<JsonEntity> save(final Long id, final JsonRequest body) {
        logger.info("Receiving '{}' for JSON id '{}'", body, id);
        final var dto = JsonDto
            .builder()
            .withId(id)
            .withSide(body.getSide())
            .withValue(body.getValue())
            .build();
        final JsonEntity result = this.service.save(dto);
        logger.info("JSON id '{}' saved.",id);
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
