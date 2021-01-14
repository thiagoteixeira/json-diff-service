package com.thiagoteixeira.jsondiffdata.vo;

import com.thiagoteixeira.jsondiffdata.dto.JsonSide;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A representation of the request object used to insert/update a JSON entity.
 *
 * @author thiagoteixeira
 */
public class JsonRequest {

    /**
     * The JSON side represented by {@link JsonSide}
     */
    @NotNull
    private JsonSide side;

    /**
     * Binary JSON data
     */
    @NotBlank
    private String value;

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
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("side", side)
            .append("value", value)
            .toString();
    }
}
