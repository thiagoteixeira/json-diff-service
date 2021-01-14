package com.thiagoteixeira.jsondiffapp.remote.entity;

/**
 * A representation of the comparison response returned by json-diff-business
 *
 * @author thiagoteixeira
 */
public class BusinessResponse {

  /**
   * The code that represents the result of the comparison and that will later be used for i18n in the FACADE layer.
   */
  private String code;

  /**
   * The result of the comparison represented by an English message.
   */
  private String message;

  /**
   * The arguments received during the assembly of the message, they will later be used for i18n in the FACADE layer.
   */
  private String arguments;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getArguments() {
    return arguments;
  }

  public void setArguments(String arguments) {
    this.arguments = arguments;
  }
}
