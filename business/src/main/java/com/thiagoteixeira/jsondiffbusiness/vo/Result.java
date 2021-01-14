package com.thiagoteixeira.jsondiffbusiness.vo;

import com.thiagoteixeira.jsondiffbusiness.service.DiffServiceImpl;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Value object used to represent the result of the {@link DiffServiceImpl#compareJsonSides(long)}
 *
 * @author thiagoteixeira
 */
public class Result {

  /**
   * Enumerator used to represent JSON content validation messages.
   */
  public enum Type {
    EQUAL_CONTENT("equal_content","The JSON contents are equal!"),
    NOT_EQUAL_SIZE("not_equal","The JSON contents have not the same size!"),
    EQUAL_SIZE_DIFFERENT_CONTENT("equal_size_different_content","The JSON contents have the same size, but offsets are different: %s")
    ;

    private String code;

    private String description;

    Type(final String code, final String description) {
      this.code = code;
      this.description = description;
    }

    public String getCode() {
      return code;
    }

    public String getDescription() {
      return description;
    }

    @Override
    public String toString() {
      return this.code;
    }
  }

  /**
   * The code that represents the result of the comparison and that will later be used for i18n in the lower layers like FACADE.
   * @see Type#code
   */
  private String code;

  /**
   * The result of the comparison represented by an English message.
   * @see Type#message
   */
  private String message;

  /**
   * The arguments received during the assembly of the message, they will later be used for i18n in the lower layers like FACADE.
   */
  private String arguments;

  public Result(final Type type) {
    this(type, null);
  }

  public Result(final Type type, final String params) {
    this.code = type.code;
    this.message = String.format(type.description, params);
    this.arguments = params;
  }

  public String getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

  public String getArguments() {
    return arguments;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
        .append("code", code)
        .append("message", message)
        .append("arguments", arguments)
        .toString();
  }
}
