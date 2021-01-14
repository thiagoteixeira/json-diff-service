package com.thiagoteixeira.jsondiffapp.remote.entity;

/**
 * A representation of the JSON entity returned by json-diff-business
 *
 * @author thiagoteixeira
 */
public class DataResponse {

  /**
   * The unique entity ID.
   */
  private long id;

  /**
   * Binary JSON data
   */
  private String left;

  /**
   * Binary JSON data
   */
  private String right;

  public long getId() {
    return id;
  }

  public void setId(long id) {
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
