package com.cypress.isupervision.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;

@Entity
public class Assistent extends User {

    private Integer projLimit;
    private Integer baLimit;
    private Integer maLimit;

    public Integer getProjLimit() {
        return projLimit;
    }
    public void setProjLimit(Integer projLimit) {
        this.projLimit = projLimit;
    }
    public Integer getBaLimit() {
        return baLimit;
    }
    public void setBaLimit(Integer baLimit) {
        this.baLimit = baLimit;
    }
    public Integer getMaLimit() {
        return maLimit;
    }
    public void setMaLimit(Integer maLimit) {
        this.maLimit = maLimit;
    }

}
