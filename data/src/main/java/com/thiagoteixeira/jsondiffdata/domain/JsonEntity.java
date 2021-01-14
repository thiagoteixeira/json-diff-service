package com.thiagoteixeira.jsondiffdata.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Entity representing an identifier and two binary JSONs (left and right)
 *
 * @author thiagoteixeira
 */
@Document
public final class JsonEntity {

    /**
     * The unique entity ID.
     */
    @Id
    private Long id;

    /**
     * Binary JSON data
     */
    private String left;

    /**
     * Binary JSON data
     */
    private String right;

    public JsonEntity() {
        super();
    }

    public JsonEntity(final Long id) {
        this.id = id;
    }

    public JsonEntity(final Long id, final String left, final String right) {
        this(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        JsonEntity that = (JsonEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(left, that.left)
            .append(right, that.right)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(left)
            .append(right)
            .toHashCode();
    }
}
