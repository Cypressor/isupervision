/*
 * iSupervision
 * ProjectEntity
 * all other project classes derive from this class
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.data.entity.project;

import com.cypress.isupervision.data.ProjectType;
import com.cypress.isupervision.data.entity.AbstractEntity;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "projectentity", uniqueConstraints = {@UniqueConstraint(columnNames = {"title"})})
public abstract class ProjectEntity extends AbstractEntity
{
    private String title;
    private String assistant;
    private String student;
    private LocalDate deadline;
    private boolean isFinished;
    private ProjectType projectType = ProjectType.Projekt;

    public ProjectEntity()
    {
        this.isFinished=false;
    }

    public ProjectType getProjectType()
    {
        return projectType;
    }
    public void setProjectType(ProjectType projectType)
    {
        this.projectType = projectType;
    }
    public boolean isFinished()
    {
        return isFinished;
    }
    public void setFinished(boolean finished)
    {
        isFinished = finished;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String titel) {this.title = titel;}
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
