package com.cypress.isupervision.data;

import com.cypress.isupervision.data.entity.project.ProjectEntity;

import java.util.Comparator;

public class ProjectEntityAssistantComparator implements Comparator<ProjectEntity>
{
    public int compare(ProjectEntity project1, ProjectEntity project2)
    {
        String assistantName1=project1.getAssistant().getFirstname().toUpperCase();
        String assistantName2=project2.getAssistant().getFirstname().toUpperCase();

        return assistantName1.compareTo(assistantName2);
    }
}