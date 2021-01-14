package com.thiagoteixeira.jsondiffapp.service;

import com.thiagoteixeira.jsondiffapp.dto.JsonDto;
import com.thiagoteixeira.jsondiffapp.remote.client.JsonClient;
import com.thiagoteixeira.jsondiffapp.remote.entity.BusinessResponse;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataRequest;
import com.thiagoteixeira.jsondiffapp.remote.entity.DataResponse;
import com.thiagoteixeira.jsondiffapp.vo.DiffResponse;
import java.util.Optional;
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
  public Optional<DataResponse> save(final JsonDto data) {
    Optional<DataResponse> response = this.client
        .create(data.getId(), new DataRequest(data.getSide(), data.getValue()));
    return response;
  }

  /**
   * {@inheritDoc}
   * @see DiffService#diff(long)
   */
  @Override
  public Optional<DiffResponse> diff(final long id) {
    final var responseOpt = this.client.diff(id);
    if(responseOpt.isPresent()){
      final BusinessResponse response = responseOpt.get();
      final var message = this.messageSource.getMessage(response.getCode(), new Object[]{response.getArguments()}, LocaleContextHolder.getLocale());
      return Optional.of(new DiffResponse(message));
    }
    return Optional.empty();
  }
}
