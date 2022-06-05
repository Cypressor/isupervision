/*
 * iSupervision
 * MeineProjekteView
 * shows users their ongoing projects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.meineprojekte;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.service.*;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.List;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("Meine Projekte")
@Route(value = "myprojects", layout = MainLayout.class)
@PermitAll
public class MeineProjekteView extends Div
{
    private Grid<MastersThesis> grid = new Grid<>(MastersThesis.class, false);
    private MastersThesis mastersThesis;
    private List<Project> projects;
    private List<Project> projects2;
    private List<BachelorsThesis> bachelorsTheses;
    private List<BachelorsThesis> bachelorsTheses2;
    private List<MastersThesis> mastersTheses;
    private List<MastersThesis> mastersTheses2;

    @Autowired
    public MeineProjekteView(AuthenticatedUser authenticatedUser, ProjectService projectService, BachelorsThesisService bachelorsThesisService, MastersThesisService mastersThesisService, AssistantService assistantService)
    {
        addClassNames("meine-projekte-view");

        projects = projectService.searchForAssistant(assistantService.get(authenticatedUser.get().get().getUsername()));
        projects2 = projectService.searchForStudent(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        bachelorsTheses = bachelorsThesisService.searchForAssistant(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        bachelorsTheses2 = bachelorsThesisService.searchForStudent(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        mastersTheses = mastersThesisService.searchForAssistant(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        mastersTheses2 = mastersThesisService.searchForStudent(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        projects.addAll(projects2);
        bachelorsTheses.addAll(bachelorsTheses2);
        mastersTheses.addAll(mastersTheses2);

        for (Project p : projects)
        {
            mastersThesis = new MastersThesis();
            mastersThesis.setTitle(p.getTitle());
            mastersThesis.setAssistant(p.getAssistant());
            mastersThesis.setStudent(p.getStudent());
            mastersThesis.setDeadline(p.getDeadline());
            mastersThesis.setFinished(p.isFinished());
            mastersThesis.setProjectType(p.getProjectType());
            mastersTheses.add(mastersThesis);
        }
        for (BachelorsThesis b : bachelorsTheses)
        {
            mastersThesis = new MastersThesis();
            mastersThesis.setTitle(b.getTitle());
            mastersThesis.setAssistant(b.getAssistant());
            mastersThesis.setStudent(b.getStudent());
            mastersThesis.setDeadline(b.getDeadline());
            mastersThesis.setFinished(b.isFinished());
            mastersThesis.setProjectType(b.getProjectType());
            mastersTheses.add(mastersThesis);
        }
        configureGrid();
        add(grid);
        grid.setItems(mastersTheses);
    }

    private void configureGrid()
    {
        grid.addColumn("title").setWidth("800px");
        grid.addColumn("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addColumn("examDate").setAutoWidth(true);
        grid.addColumn("projectType").setAutoWidth(true);
        grid.addComponentColumn(mastersTheses -> createFinishedIcon(mastersTheses.isFinished())).setHeader("Abgeschlossen");

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");
        grid.getColumnByKey("examDate").setHeader("Prüfungstermin");
        grid.getColumnByKey("projectType").setHeader("Projekt-Art");
    }

    private Icon createFinishedIcon(boolean isFinished)
    {
        Icon icon;
        if(isFinished)
        {
            icon = createIcon(VaadinIcon.CHECK, "Ja");
            icon.getElement().getThemeList().add("badge success");
        }
        else
        {
            icon = createIcon(VaadinIcon.CLOSE_SMALL,"Nein");
            icon.getElement().getThemeList().add("badge error");
        }
        return icon;
    }

    private Icon createIcon(VaadinIcon vaadinIcon, String label)
    {
     Icon icon = vaadinIcon.create();
     icon.getStyle().set("padding","var(--lumo-space-xs");
     icon.getElement().setAttribute("aria-lavel",label);
     icon.getElement().setAttribute("title", label);
     return icon;
    }
}


