package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.entity.AbstractEntity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Projekt extends AbstractEntity
{

    private String titel;
    private String assistant;
    private String student;
    private LocalDate deadline;

    public String getTitel() {
        return titel;
    }
    public void setTitel(String titel) {
        this.titel = titel;
    }
    public String getAssistant() {
        return assistant;
    }
    public void setAssistant(String assistant) {
        this.assistant = assistant;
    }
    public String getStudent() {
        return student;
    }
    public void setStudent(String student) {
        this.student = student;
    }
    public LocalDate getDeadline() {
        return deadline;
    }
    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

}
