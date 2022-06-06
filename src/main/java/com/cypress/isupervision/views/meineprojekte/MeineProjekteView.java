/*
 * iSupervision
 * MeineProjekteView
 * shows users their ongoing projects
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.meineprojekte;

import com.cypress.isupervision.data.ProjectType;
import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.*;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import java.util.ArrayList;
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
    private List<BachelorsThesis> bachelorsTheses = new ArrayList<>();
    private List<MastersThesis> mastersTheses = new ArrayList<>();
    private Assistant assistant= new Assistant();
    private Student student = new Student();
    private AuthenticatedUser authenticatedUser;
    private ProjectService projectService;
    private BachelorsThesisService bachelorsThesisService;
    private MastersThesisService mastersThesisService;
    private StudentService studentService;
    private AssistantService assistantService;
    private AdministratorService administratorService;

    @Autowired
    public MeineProjekteView(AuthenticatedUser authenticatedUser, ProjectService projectService, BachelorsThesisService bachelorsThesisService, MastersThesisService mastersThesisService, StudentService studentService, AssistantService assistantService, AdministratorService administratorService)
    {
        this.authenticatedUser=authenticatedUser;
        this.projectService=projectService;
        this.bachelorsThesisService=bachelorsThesisService;
        this.mastersThesisService=mastersThesisService;
        this.studentService=studentService;
        this.assistantService=assistantService;
        this.administratorService=administratorService;

        addClassNames("meine-projekte-view");
        fetchAllProjects();
        reformatProjects();
        configureGrid();
        add(grid);
        grid.setItems(mastersTheses);
    }

    private void reformatProjects()
    {
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
    }

    private void fetchAllProjects()
    {
        if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
        {
            assistant = administratorService.get(authenticatedUser.get().get().getUsername());
            projects = projectService.searchForAssistant(assistant);
            bachelorsTheses = bachelorsThesisService.searchForAssistant(assistant);
            mastersTheses = mastersThesisService.searchForAssistant(assistant);
        }
        else if (authenticatedUser.get().get().getRoles().contains(Role.ASSISTANT) && !(authenticatedUser.get().get().getRoles().contains(Role.ADMIN)))
        {
            assistant = assistantService.get(authenticatedUser.get().get().getUsername());
            projects = projectService.searchForAssistant(assistant);
            bachelorsTheses = bachelorsThesisService.searchForAssistant(assistant);
            mastersTheses = mastersThesisService.searchForAssistant(assistant);
        }
        else if (authenticatedUser.get().get().getRoles().contains(Role.STUDENT) && !(authenticatedUser.get().get().getRoles().contains(Role.ADMIN)))
        {
            student = studentService.get(authenticatedUser.get().get().getUsername());
            projects = projectService.searchForStudent(student);
            bachelorsTheses = bachelorsThesisService.searchForStudent(student);
            mastersTheses = mastersThesisService.searchForStudent(student);
        }
    }

    private void configureGrid()
    {
        grid.addColumn("title").setWidth("800px");
        grid.addColumn(mastersThesis -> mastersThesis.getAssistant().getFirstname() + " " + mastersThesis.getAssistant().getLastname(), "assistant.firstname")
                .setHeader("Assistent")
                .setKey("assistant")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(mastersThesis -> fillStudentColumn(mastersThesis), "student.firstname")
                .setHeader("Student")
                .setKey("student")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addColumn("examDate").setAutoWidth(true);
        grid.addColumn(mastersThesis -> reformatProjectType(mastersThesis))
                .setAutoWidth(true)
                .setHeader("Projekt-Art")
                .setKey("projectType")
                .setSortable(true);
        grid.addComponentColumn(mastersTheses -> createFinishedIcon(mastersTheses.isFinished())).setHeader("Abg.");
        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("deadline").setHeader("Deadline");
        grid.getColumnByKey("examDate").setHeader("Prüfungstermin");
    }

    private String fillStudentColumn(MastersThesis mastersThesis)
    {

        String student="";
        if(mastersThesis.getStudent()!=null && !mastersThesis.getStudent().equals(""))
        {
            student = mastersThesis.getStudent().getFirstname() + " " + mastersThesis.getStudent().getLastname();
        }
        return student;
    }

    private String reformatProjectType(MastersThesis mastersThesis)
    {
        String returnString="Kein";
        if(mastersThesis.getProjectType()== ProjectType.PROJECT)
        {
            returnString="Projekt";
        }
        if(mastersThesis.getProjectType()== ProjectType.BACHELORSTHESIS)
        {
            returnString="Bachelorarbeit";
        }
        if(mastersThesis.getProjectType()== ProjectType.MASTERSTHESIS)
        {
            returnString="Masterarbeit";
        }

        return returnString;
    }

    private Icon createFinishedIcon(boolean isFinished)
    {
        Icon icon;
        if (isFinished)
        {
            icon = createIcon(VaadinIcon.CHECK, "Ja");
            icon.getElement().getThemeList().add("badge success");
        } else
        {
            icon = createIcon(VaadinIcon.CLOSE_SMALL, "Nein");
            icon.getElement().getThemeList().add("badge error");
        }
        return icon;
    }

    private Icon createIcon(VaadinIcon vaadinIcon, String label)
    {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        icon.getElement().setAttribute("aria-lavel", label);
        icon.getElement().setAttribute("title", label);
        return icon;
    }
}


