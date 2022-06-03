package com.cypress.isupervision.data.entity.user;

import com.cypress.isupervision.data.Role;
import javax.persistence.Entity;
import java.util.Set;

@Entity
public class Administrator extends User {

    private Integer projLimit;
    private Integer baLimit;
    private Integer maLimit;

    public Administrator()
    {
        this.setRoles(Set.of(Role.ADMIN, Role.ASSISTANT, Role.STUDENT));

        this.projLimit=1000;
        this.baLimit=1000;
        this.maLimit=1000;
    }


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
