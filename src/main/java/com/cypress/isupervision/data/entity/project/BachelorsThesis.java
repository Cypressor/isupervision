/*
 * iSupervision
 * BachelorsThesis
 * extends ProjectEntity, represents a BachelorsThesis
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;
import javax.persistence.Entity;

@Entity
public class BachelorsThesis extends ProjectEntity
{
    public BachelorsThesis()
    {
        this.setProjectType(ProjectType.BACHELORSTHESIS);
    }
}
