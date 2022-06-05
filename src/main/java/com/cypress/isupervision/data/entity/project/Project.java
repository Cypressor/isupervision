/*
 * iSupervision
 * Project
 * extends ProjectEntity, represents a Project
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;
import javax.persistence.Entity;

@Entity
public class Project extends ProjectEntity
{
    public Project()
    {
        this.setProjectType(ProjectType.PROJECT);
    }
}
