package com.company.model.jpa;

import java.io.Serializable;
import java.time.ZonedDateTime;

public interface Auditable<U extends Serializable, ID extends Serializable> extends Persistable<ID> {

    ZonedDateTime getCreatedDateTime();
    void setCreatedDateTime(ZonedDateTime createdDateTime);
    ZonedDateTime getLastModifiedDateTime();
    void setLastModifiedDateTime(ZonedDateTime lastModifiedDateTime);
    U getCreatedBy();
    void setCreatedBy(U createdBy);
    U getLastModifiedBy();
    void setLastModifiedBy(U lastModifiedBy);

}
