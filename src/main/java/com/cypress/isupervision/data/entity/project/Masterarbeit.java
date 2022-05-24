package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.entity.AbstractEntity;

import java.time.LocalDate;
import javax.persistence.Entity;

@Entity
public class Masterarbeit extends AbstractEntity
{

    private String titel;
    private String assistent;
    private String student;
    private LocalDate deadline;
    private LocalDate pruefungstermin;

    public String getTitel() {
        return titel;
    }
    public void setTitel(String titel) {
        this.titel = titel;
    }
    public String getAssistent() {
        return assistent;
    }
    public void setAssistent(String assistent) {
        this.assistent = assistent;
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
    public LocalDate getPruefungstermin() {
        return pruefungstermin;
    }
    public void setPruefungstermin(LocalDate pruefungstermin) {
        this.pruefungstermin = pruefungstermin;
    }

}
