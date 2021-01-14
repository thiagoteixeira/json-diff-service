package com.thiagoteixeira.jsondiffdata.dto;

/**
 * Data transfer object used to transfer the {@link com.thiagoteixeira.jsondiffdata.vo.JsonRequest} data information
 * from {@link com.thiagoteixeira.jsondiffdata.resource.JsonResourceImpl} to
 * {@link com.thiagoteixeira.jsondiffdata.service.JsonServiceImpl} (service layer)
 *
 * @author thiagoteixeira
 */
public class JsonDto {

  /**
   * The unique entity ID.
   */
  private Long id;

  /**
   * It represents the JSON side that will be later saved into {@link com.thiagoteixeira.jsondiffdata.domain.JsonEntity} database entity.
   */
  private JsonSide side;

  /**
   * It represents the JSON value that will be later saved into {@link com.thiagoteixeira.jsondiffdata.domain.JsonEntity} database entity.
   */
  private String value;

  private JsonDto() {
    super();
  }

  public Long getId() {
    return id;
  }

  public JsonSide getSide() {
    return side;
  }

  public String getValue() {
    return value;
  }

  public static JsonDtoBuilder builder(){
    return new JsonDtoBuilder();
  }
  public static class JsonDtoBuilder {
    private JsonDto instance = new JsonDto();

    public JsonDtoBuilder withId(final Long id){
      this.instance.id = id;
      return this;
    }

    public JsonDtoBuilder withSide(final JsonSide side){
      this.instance.side = side;
      return this;
    }

    public JsonDtoBuilder withValue(final String value){
      this.instance.value = value;
      return this;
    }

    public JsonDto build(){
      return this.instance;
    }
  }
}
