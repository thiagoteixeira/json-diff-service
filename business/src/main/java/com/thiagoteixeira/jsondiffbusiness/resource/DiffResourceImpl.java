package com.thiagoteixeira.jsondiffbusiness.resource;

import com.thiagoteixeira.jsondiffbusiness.service.DiffService;
import com.thiagoteixeira.jsondiffbusiness.vo.Result;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@inheritDoc}
 * @see DiffResource
 *
 * @author thiagoteixeira
 */
@RestController
public class DiffResourceImpl implements DiffResource {

  private static final Logger logger = LoggerFactory.getLogger(DiffResourceImpl.class);

  private final DiffService service;

  @Autowired
  public DiffResourceImpl(final DiffService service) {
    this.service = service;
  }

  /**
   * {@inheritDoc}
   * @see DiffResource#getDiff(Long)
   */
  @Override
  public ResponseEntity<Result> getDiff(final Long id) {
    logger.info("Starting JSON sides comparison for '{}'", id);
    Optional<Result> result = this.service.compareJsonSides(id);
    if(result.isPresent()) {
      logger.info("JSON side comparison has been finished and the result was '{}'", result.get().getCode());
      return ResponseEntity.ok(result.get());
    }
    logger.info("JSON '{}' was not found so the comparison has been skipped", id);
    return ResponseEntity.notFound().build();
  }
}
