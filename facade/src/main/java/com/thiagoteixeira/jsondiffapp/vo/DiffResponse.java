package com.thiagoteixeira.jsondiffapp.vo;

/**
 * Value object used to represent the result of the {@link com.thiagoteixeira.jsondiffapp.service.DiffServiceImpl#diff(long)}
 *
 * @author thiagoteixeira
 */
public class DiffResponse {

  /**
   * The result of the comparison represented by a message given a language received in the request or in a standard language.
   */
  private String message;

  public DiffResponse(final String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

}
