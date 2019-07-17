package com.company.model.jpa;

import org.springframework.lang.Nullable;

import java.io.Serializable;

public interface Persistable<ID extends Serializable> extends Serializable {

    /**
     * Returns the id of the entity.
     *
     * @return the id
     */
    @Nullable
    ID getId();

    /**
     * Returns the version of the entity
     * @return the version
     */
    default Integer getVersion() {
        return null;
    }

    /**
     * Returns if the {@code Persistable} is new or was persisted already.
     * Uses the version field instead of the id field. This does not rule out any id generation strategy (as opposed to using the id field).
     * @return if the object is new
     */
    default boolean isNew() {
        return getVersion() == null;
    }

}
