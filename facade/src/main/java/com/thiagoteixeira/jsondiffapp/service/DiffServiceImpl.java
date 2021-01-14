package com.thiagoteixeira.jsondiffapp.service;

import com.thiagoteixeira.jsondiffapp.dto.JsonDto;
import com.thiagoteixeira.jsondiffapp.exception.FacadeException;
import com.thiagoteixeira.jsondiffapp.remote.client.JsonClient;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * {@inheritDoc}
 * @see DiffService
 *
 * @author thiagoteixeira
 */
@Component
public class DiffServiceImpl implements DiffService {

  private static final Logger logger = LoggerFactory.getLogger(DiffServiceImpl.class);

  private final JsonClient client;

  @Autowired
  private final MessageSource messageSource;

  @Autowired
  public DiffServiceImpl(JsonClient client, MessageSource messageSource) {
    this.client = client;
    this.messageSource = messageSource;
  }

  /**
   * {@inheritDoc}
   * @see DiffService#save(JsonDto) 
   */
  @Override
  public DataResponse save(final JsonDto data) {
    logger.info("Calling json-diff-data service to save '{}' JSON entity side", data);
    final var savedData = this.client
        .create(data.getId(), new DataRequest(data.getSide(), data.getValue()));
    if(savedData.isPresent()) {
      logger.info("The JSON entity '{}' has been saved successful", data.getId());
      return savedData.get();
    }
    logger.error("The JSON entity '{}' has not been saved", data);
    throw new FacadeException("The JSON entity has not been created");
  }

  /**
   * {@inheritDoc}
   * @see DiffService#diff(long)
   */
  @Override
  public Optional<DiffResponse> diff(final long id) {
    logger.info("Calling json-diff-business service to compare sides from the JSON entity id '{}'", id);
    final var responseOpt = this.client.diff(id);
    if(responseOpt.isPresent()){
      logger.info("The JSON entity '{}' has been compared successful", id);
      final BusinessResponse response = responseOpt.get();
      final var message = this.messageSource.getMessage(response.getCode(), new Object[]{response.getArguments()}, LocaleContextHolder.getLocale());
      return Optional.of(new DiffResponse(message));
    }
    logger.info("The JSON entity '{}' has not been found", id);
    return Optional.empty();
  }
}
