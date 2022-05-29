package com.cypress.isupervision.data.entity.user;

import com.cypress.isupervision.data.Role;
import javax.persistence.Entity;
import java.util.Collections;

@Entity
public class Assistant extends User {

    private Integer projLimit;
    private Integer baLimit;
    private Integer maLimit;

    public Assistant()
    {
        this.setRoles(Collections.singleton(Role.ASSISTANT));
        this.projLimit=1000;
        this.baLimit=1000;
        this.maLimit=1000;
        this.setProfilePictureUrl("https://upload.wikimedia.org/wikipedia/commons/f/f8/Burt_Ward_Robin.jpg");
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
