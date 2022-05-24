package com.cypress.isupervision.data.entity.user;

import com.cypress.isupervision.data.Role;
import javax.persistence.Entity;
import java.util.Collections;

@Entity
public class Student extends User {

    private Integer level;

    public Student()
    {
        this.setRoles(Collections.singleton(Role.STUDENT));
        this.level=0;
    }

    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

}
