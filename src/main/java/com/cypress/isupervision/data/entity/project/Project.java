package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;
import javax.persistence.Entity;

@Entity
public class Project extends ProjectEntity
{
    public Project()
    {
        this.setProjectType(ProjectType.Projekt);
    }
}
