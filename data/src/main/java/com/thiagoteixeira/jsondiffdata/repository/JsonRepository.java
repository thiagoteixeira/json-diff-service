package com.thiagoteixeira.jsondiffdata.repository;

import com.thiagoteixeira.jsondiffdata.domain.JsonEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * A {@link MongoRepository} instance to allow {@link JsonEntity} persistence.
 *
 * @author thiagoteixeira
 */
@Repository
public interface JsonRepository extends MongoRepository<JsonEntity, Long> {
}
