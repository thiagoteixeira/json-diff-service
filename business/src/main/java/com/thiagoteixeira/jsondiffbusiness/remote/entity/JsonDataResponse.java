package com.thiagoteixeira.jsondiffbusiness.remote.entity;

/**
 * A JSON entity representation from the json-diff-data microservice.
 *
 * @author thiagoteixeira
 */
public class JsonDataResponse {

  /**
   * The unique entity ID.
   */
  private Long id;

  /**
   * Binary JSON data
   */
  private String left;

  /**
   * Binary JSON data
   */
  private String right;

  public JsonDataResponse() {
    super();
  }

  public JsonDataResponse(final Long id, final String left, final String right) {
    this.id = id;
    this.left = left;
    this.right = right;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getLeft() {
    return left;
  }

  public void setLeft(String left) {
    this.left = left;
  }

  public String getRight() {
    return right;
  }

  public void setRight(String right) {
    this.right = right;
  }
}
