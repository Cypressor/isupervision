package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;
import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class MastersThesis extends ProjectEntity
{
    private LocalDate examDate;

    public MastersThesis()
    {
        this.setProjectType(ProjectType.Masterarbeit);
    }

    public LocalDate getExamDate()
    {
        return examDate;
    }
    public void setExamDate(LocalDate examDate)
    {
        this.examDate = examDate;
    }

}
