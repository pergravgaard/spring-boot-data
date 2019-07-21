package com.company.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
public class Car extends BaseAuditableEntity<String, Long> {

    @NotBlank
    @Column(nullable = false)
    private String model;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
