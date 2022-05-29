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
        this.setProfilePictureUrl("https://imagesvc.meredithcorp.io/v3/mm/image?url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F13%2F2016%2F06%2F03%2Fron.jpg&q=60");
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
}
