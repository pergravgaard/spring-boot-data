package com.company.model.jpa;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class}) // AuditingEntityListener will also audit any subclasses of BaseAuditableEntity...
public class BaseAuditableEntity<U extends Serializable, ID extends Serializable> extends BaseEntity<ID> implements Auditable<U, ID> {

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private ZonedDateTime createdDateTime;

    @Column(nullable = false)
    @LastModifiedDate
    private ZonedDateTime lastModifiedDateTime;

    @CreatedBy
    private U createdBy;

    @LastModifiedBy
    private U lastModifiedBy;

    public ZonedDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(ZonedDateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public ZonedDateTime getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(ZonedDateTime lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    @Override
    public U getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(U createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public U getLastModifiedBy() {
        return lastModifiedBy;
    }

    @Override
    public void setLastModifiedBy(U lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

}
