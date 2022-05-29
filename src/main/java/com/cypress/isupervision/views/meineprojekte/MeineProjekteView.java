package com.cypress.isupervision.views.meineprojekte;

import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.service.*;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
    AuthenticatedUser authenticatedUser;
    List<Project> projects;
    List<Project> projects2;
    List<BachelorsThesis> bachelorsTheses;
    List<BachelorsThesis> bachelorsTheses2;
    List<MastersThesis> mastersTheses;
    List<MastersThesis> mastersTheses2;

@Autowired
    public MeineProjekteView(AuthenticatedUser authenticatedUser, ProjectService projectService, BachelorsThesisService bachelorsThesisService, MastersThesisService mastersThesisService)
{
        this.authenticatedUser=authenticatedUser;
        addClassNames("meine-projekte-view");

    projects=projectService.searchForAssistant(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    projects2=projectService.searchForStudent(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    bachelorsTheses=bachelorsThesisService.searchForAssistant(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    bachelorsTheses2=bachelorsThesisService.searchForStudent(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    mastersTheses=mastersThesisService.searchForAssistant(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    mastersTheses2=mastersThesisService.searchForStudent(authenticatedUser.get().get().getFirstname()+ " " + authenticatedUser.get().get().getLastname());
    projects.addAll(projects2);
    bachelorsTheses.addAll(bachelorsTheses2);
    mastersTheses.addAll(mastersTheses2);

    for(Project p : projects)
    {
        mastersThesis = new MastersThesis();
        mastersThesis.setTitle(p.getTitle());
        mastersThesis.setAssistant(p.getAssistant());
        mastersThesis.setStudent(p.getStudent());
        mastersThesis.setDeadline(p.getDeadline());
        mastersTheses.add(mastersThesis);
    }
    for(BachelorsThesis b : bachelorsTheses)
    {
        mastersThesis = new MastersThesis();
        mastersThesis.setTitle(b.getTitle());
        mastersThesis.setAssistant(b.getAssistant());
        mastersThesis.setStudent(b.getStudent());
        mastersThesis.setDeadline(b.getDeadline());
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

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");
        grid.getColumnByKey("examDate").setHeader("Prüfungstermin");
    }
}


