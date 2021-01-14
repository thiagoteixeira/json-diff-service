package com.thiagoteixeira.jsondiffapp.remote.entity;

import com.thiagoteixeira.jsondiffapp.dto.JsonSide;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * A representation of the request body expected by json-diff-data microservice during the JSON entity creation.
 *
 * @author thiagoteixeira
 */
public class DataRequest {

  /**
   * The JSON side represented by {@link JsonSide}
   */
  private JsonSide side;

  /**
   * Binary JSON data
   */
  private String value;

  public DataRequest() {
    super();
  }

  public DataRequest(final JsonSide side, final String value) {
    this.side = side;
    this.value = value;
  }

  public JsonSide getSide() {
    return side;
  }

  public void setSide(JsonSide side) {
    this.side = side;
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
        .append(side, that.side)
        .append(value, that.value)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(side)
        .append(value)
        .toHashCode();
  }
}
