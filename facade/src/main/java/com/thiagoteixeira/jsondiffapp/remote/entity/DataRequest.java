package com.thiagoteixeira.jsondiffapp.remote.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A representation of the request body expected by json-diff-data microservice during the JSON entity creation.
 *
 * @author thiagoteixeira
 */
public class DataRequest {

  /**
   * Binary JSON data
   */
  private String value;

  public DataRequest() {
    super();
  }

  public DataRequest(final String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    DataRequest that = (DataRequest) o;

    return new EqualsBuilder()
        .append(value, that.value)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(value)
        .toHashCode();
  }
}
