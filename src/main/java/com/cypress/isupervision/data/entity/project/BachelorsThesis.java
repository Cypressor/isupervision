package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;

import javax.persistence.Entity;

@Entity
public class BachelorsThesis extends ProjectEntity
{

    public BachelorsThesis()
    {
        this.setProjectType(ProjectType.Bachelorarbeit);
    }
}
