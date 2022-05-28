package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.entity.AbstractEntity;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class ProjectEntity extends AbstractEntity
{
    private String title;
    private String assistant;
    private String student;
    private LocalDate deadline;

    public String getTitle() {
        return title;
    }
    public void setTitle(String titel) {
        this.title = titel;
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
