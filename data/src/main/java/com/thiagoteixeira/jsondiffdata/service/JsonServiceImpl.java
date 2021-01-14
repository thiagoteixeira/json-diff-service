package com.thiagoteixeira.jsondiffdata.service;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import com.thiagoteixeira.jsondiffdata.dto.JsonDto;
import com.thiagoteixeira.jsondiffdata.dto.JsonSide;
import com.thiagoteixeira.jsondiffdata.repository.JsonRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@inheritDoc}
 *
 * @see JsonService
 *
 * @author thiagoteixeira
 */
@Service
public class JsonServiceImpl implements JsonService {

    private static final Logger logger = LoggerFactory.getLogger(JsonServiceImpl.class);

    private final JsonRepository repository;

    @Autowired
    public JsonServiceImpl(final JsonRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     * 
     * @see JsonService#save(JsonDto) 
     */
    @Override
    public JsonEntity save(final JsonDto dto) {
        final var data = repository.findById(dto.getId()).orElse(new JsonEntity(dto.getId()));
        if(JsonSide.RIGHT.equals(dto.getSide())){
            logger.info("Saving right side value '{}' in JSON id '{}'", dto.getValue(), dto.getId());
            data.setRight(dto.getValue());
        } else {
            logger.info("Saving left side value '{}' in JSON id '{}'", dto.getValue(), dto.getId());
            data.setLeft(dto.getValue());
        }
        final var result = this.repository.save(data);
        logger.info("JSON id '{}' was successfully saved", dto.getId());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see JsonService#find(Long) 
     * 
     */
    @Override
    public Optional<JsonEntity> find(final Long id) {
        logger.info("Retrieving JSON id '{}'", id);
        final var result = this.repository.findById(id);
        logger.info("JSON id '{}' was successfully retrieved", id);
        return result;
    }
}
