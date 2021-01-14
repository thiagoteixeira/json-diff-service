package com.thiagoteixeira.jsondiffdata.vo;

import javax.validation.constraints.NotBlank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A representation of the request object used to insert/update a JSON entity.
 *
 * @author thiagoteixeira
 */
public class JsonRequest {

    /**
     * Binary JSON data
     */
    @NotBlank
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .append("value", value)
            .toString();
    }
}
