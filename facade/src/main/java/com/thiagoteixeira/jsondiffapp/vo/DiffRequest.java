package com.thiagoteixeira.jsondiffapp.vo;

/**
 * Value object used to represent the request object that includes the JSON Base64 value.
 *
 * @author thiagoteixeira
 */
public class DiffRequest {

  /**
   * The JSON Base64 value representation
   */
  private String value;

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
