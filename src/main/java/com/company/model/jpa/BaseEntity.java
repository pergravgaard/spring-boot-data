package com.company.model.jpa;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class BaseEntity<ID extends Serializable> implements Persistable<ID> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "native"
    )
    @GenericGenerator(
            name = "native",
            strategy = "native"
    )
    private ID id;

    @Version
    private Integer version;

    @Override
    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public Integer getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // when we let the JPA provider auto generate the primary key, we must use the primary key in equals and hashCode
    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        BaseEntity that = (BaseEntity) obj;
        // if none of them have an id they're not equal, if one of them have no id, they're not equal either
        if (getId() == null || that.getId() == null) {
            return super.equals(obj);
        }
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId() != null ? Objects.hashCode(getId()) : super.hashCode();
    }

}
