package com.cypress.isupervision.data.entity.project;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class MastersThesis extends Project
{

    private LocalDate examDate;

    public LocalDate getExamDate()
    {
        return examDate;
    }
    public void setExamDate(LocalDate examDate)
    {
        this.examDate = examDate;
    }

}
