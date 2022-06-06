/*
 * iSupervision
 * ProjekteAssistentenView
 * Projekte view for assistant users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.projekte;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.*;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.RolesAllowed;
import org.springframework.data.domain.PageRequest;

@PageTitle("Projekte Assistenten")
@Route(value = "projects/assistant/:projectID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ASSISTANT", "ADMIN"})
public class ProjekteAssistentenView extends Div implements BeforeEnterObserver
{
    private final String PROJECT_ID = "projectID";
    private final String PROJECT_EDIT_ROUTE_TEMPLATE = "projects/assistant/%s/edit";
    private final ProjectService projectService;
    private final AssistantService assistantService;
    private final StudentService studentService;
    private final AuthenticatedUser authenticatedUser;
    private final ProjectEntityService projectEntityService;
    private final AdministratorService administratorService;
    private Grid<Project> grid = new Grid<>(Project.class, false);
    private TextField title;
    private ComboBox<Assistant> assistant = new ComboBox<>("Assistent");
    private ComboBox<Student> student = new ComboBox<>("Student (optional)");
    private DatePicker deadline;
    private Checkbox finished;
    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button edit = new Button("Ändern");
    private BeanValidationBinder<Project> binder;
    private Project project;
    private Dialog warning = new Dialog();
    private List<Project> projects;
    private int limit;

    public ProjekteAssistentenView(AuthenticatedUser authenticatedUser, ProjectService projectService, ProjectEntityService projectEntityService, StudentService studentService, AssistantService assistantService, AdministratorService administratorService)
    {
        this.authenticatedUser = authenticatedUser;
        this.projectService = projectService;
        this.assistantService = assistantService;
        this.studentService = studentService;
        this.projectEntityService = projectEntityService;
        this.administratorService = administratorService;
        addClassNames("projekte-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);
        createGrid();
        //Hook up buttons
        reactToGridSelection();
        fillAssistantField();
        configureBinder();
        cancelButtonListener();
        saveButtonListener();
        deleteButtonListener();
        editButtonListener();
    }

    private void editButtonListener()
    {
        //Hook up Edit Button
        edit.addClickListener(e ->
        {
            try
            {
                if (this.project != null)
                {
                    if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || authenticatedUser.get().get().getUsername().equals(this.project.getAssistant().getUsername()))
                    {
                        Assistant projectAssistant=this.project.getAssistant();
                        binder.writeBean(this.project);
                        if (title.getValue().trim().equals(""))
                        {
                            Notification.show("Bitte Titel eintragen.");
                        } else
                        {
                            if (assistant.isEmpty())
                            {
                                Notification.show("Bitte Assistent eintragen.");
                            } else
                            {
                                if (deadline.isEmpty())
                                {
                                    Notification.show("Bitte Deadline eintragen.");
                                }
                            }
                        }
                        if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || (assistantService.get(authenticatedUser.get().get().getUsername()).equals(assistant.getValue())))
                        {
                            if (!title.getValue().trim().equals("") && !assistant.isEmpty() && !deadline.isEmpty())
                            {
                                projects=projectService.searchForAssistant(assistantService.get(authenticatedUser.get().get().getUsername()));
                                if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
                                {

                                    limit=administratorService.get(authenticatedUser.get().get().getUsername()).getProjLimit();
                                }
                                else
                                {
                                    limit=assistantService.get(authenticatedUser.get().get().getUsername()).getProjLimit();
                                }
                                if (projects.size()<limit || projectAssistant.getUsername().equals(authenticatedUser.get().get().getUsername()))
                                {
                                    projectService.update(this.project);
                                    clearForm();
                                    refreshGrid();
                                    Notification.show("Projekt wurde bearbeitet.");

                                }
                                else
                                {
                                    Notification.show("Ihr Limit an Projekten ist bereits erreicht.");
                                }
                            }
                        } else
                        {
                            Notification.show("Assistenten dürfen nur Projekte auf ihren eigenen Namen eintragen.");
                        }
                    } else
                    {
                        Notification.show("Assistenten dürfen nur ihre eigenen Projekte verändern.");
                    }
                } else
                {
                    Notification.show("Kein Projekt ausgewählt.");
                }
                UI.getCurrent().navigate(ProjekteAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });
    }

    private void deleteButtonListener()
    {
        //Hook up Delete Button
        delete.addClickListener(e ->
        {
            if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || authenticatedUser.get().get().getUsername().equals(this.project.getAssistant().getUsername()))
            {
                warning.open();
            } else
            {
                Notification.show("Assistenten dürfen ihre eigenen Projekte löschen.");
            }
        });
    }

    private void saveButtonListener()
    {
        //Hook up Save Button
        save.addClickListener(e ->
        {
            try
            {
                this.project = new Project();
                binder.writeBean(this.project);

                if (title.getValue().trim().equals(""))
                {
                    Notification.show("Bitte Titel eintragen.");
                } else
                {
                    if (assistant.isEmpty())
                    {
                        Notification.show("Bitte Assistent eintragen.");
                    } else
                    {
                        if (deadline.isEmpty())
                        {
                            Notification.show("Bitte Deadline eintragen.");
                        }
                    }
                }
                if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || assistantService.get(authenticatedUser.get().get().getUsername()).equals(assistant.getValue()))
                {
                    if (!title.getValue().trim().equals("") && !assistant.isEmpty() && !deadline.isEmpty())
                    {
                        if (this.project != null)
                        {
                            int exists = projectEntityService.exists(this.project);
                            if (exists == 0)
                            {
                                projects=projectService.searchForAssistant(assistantService.get(authenticatedUser.get().get().getUsername()));
                                if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
                                {

                                    limit=administratorService.get(authenticatedUser.get().get().getUsername()).getProjLimit();
                                }
                                else
                                {
                                    limit=assistantService.get(authenticatedUser.get().get().getUsername()).getProjLimit();
                                }
                                if (projects.size()<limit)
                                {
                                    projectService.update(this.project);
                                    clearForm();
                                    refreshGrid();
                                    Notification.show("Projekt gespeichert.");
                                }
                                else
                                {
                                    Notification.show("Ihr Limit an Projekten ist bereits erreicht.");
                                }
                            } else
                            {
                                Notification.show("Titel existiert bereits.");
                            }
                        }
                    }
                } else
                {
                    Notification.show("Assistenten dürfen nur Projekte auf ihren eigenen Namen buchen.");
                }
                UI.getCurrent().navigate(ProjekteAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });
    }

    private void cancelButtonListener()
    {
        //Hook up Cancel Button
        cancel.addClickListener(e ->
        {
            clearForm();
            refreshGrid();
        });
    }

    private void configureBinder()
    {
        // Configure Form
        binder = new BeanValidationBinder<>(Project.class);
        // Bind fields. This is where you'd define e.g. validation rules
        binder.bindInstanceFields(this);
    }

    private void reactToGridSelection()
    {
        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event ->
        {
            if (event.getValue() != null)
            {
                UI.getCurrent().navigate(String.format(PROJECT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else
            {
                clearForm();
                UI.getCurrent().navigate(ProjekteAssistentenView.class);
            }
        });
    }

    private void createGrid()
    {
        // Configure Grid
        grid.addColumn("title").setWidth("800px");
        grid.addColumn(project -> project.getAssistant().getFirstname()+" "+project.getAssistant().getLastname(), "assistant.firstname")
                .setHeader("Assistent")
                .setKey("assistant")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn(project -> fillStudentColumn(project), "student.firstname")
                .setHeader("Student")
                .setKey("student")
                .setAutoWidth(true)
                .setSortable(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addComponentColumn(projectFinished -> createFinishedIcon(projectFinished.isFinished()))
                .setHeader("Abg.")
                .setKey("finished")
                .setAutoWidth(true);
        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("deadline").setHeader("Deadline");

        grid.setItems(query -> projectService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        Optional<UUID> projektId = event.getRouteParameters().get(PROJECT_ID).map(UUID::fromString);
        if (projektId.isPresent())
        {
            Optional<Project> projektFromBackend = projectService.get(projektId.get());
            if (projektFromBackend.isPresent())
            {
                populateForm(projektFromBackend.get());
            } else
            {
                Notification.show(String.format("Projekt wurde nicht gefunden, ID = %s", projektId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(ProjekteAssistentenView.class);
            }
        }
    }

    private String fillStudentColumn(Project project)
    {

        String student="";
        if(project.getStudent()!=null && !project.getStudent().equals(""))
        {
            student = project.getStudent().getFirstname() + " " + project.getStudent().getLastname();
        }
        return student;
    }

    private void createAssistantBox()
    {
        assistant.setAllowCustomValue(false);
        assistant.setPlaceholder("Assistent auswählen");
        List<Assistant> assistants = assistantService.getAll();
        assistant.setItems(assistants);
        assistant.setItemLabelGenerator(person -> person.getFirstname() + " " + person.getLastname());
    }

    private void createStudentBox()
    {
        student.setAllowCustomValue(false);
        student.setPlaceholder("Student auswählen");
        List<Student> students = studentService.getAll();
        student.setItems(students);
        student.setItemLabelGenerator(person -> person.getFirstname() + " " + person.getLastname());
    }

    private void createEditorLayout(SplitLayout splitLayout)
    {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");
        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        title = new TextField("Titel");
        createAssistantBox();
        createStudentBox();
        deadline = new DatePicker("Deadline");
        finished = new Checkbox("Abgeschlossen");
        Component[] fields = new Component[]{title, assistant, student, deadline, finished};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);
        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv)
    {
        VerticalLayout buttonLayout1 = new VerticalLayout();
        VerticalLayout buttonLayout2 = new VerticalLayout();
        buttonLayout1.setClassName("button-layout1");
        buttonLayout2.setClassName("button-layout2");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        edit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout1.add(save, cancel);
        buttonLayout2.add(edit, delete);
        HorizontalLayout greaterButtonLayout = new HorizontalLayout();
        buttonLayout2.setAlignItems(FlexComponent.Alignment.STRETCH);
        buttonLayout1.setAlignItems(FlexComponent.Alignment.STRETCH);
        greaterButtonLayout.add(buttonLayout1, buttonLayout2);
        greaterButtonLayout.setVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        editorLayoutDiv.add(greaterButtonLayout);
    }

    private void createDialog()
    {
        warning.add(new H4("Löschen"));
        warning.add(new Paragraph("Sind Sie sicher, dass Sie dieses Projekt löschen möchten?"));
        Button delete = new Button("Löschen");
        Button cancel = new Button("Abbrechen");
        warning.add(delete, cancel);
        delete.addClickListener(event -> {confirmDelete();});
        cancel.addClickListener(event->{cancelDelete();});
    }
    private void confirmDelete()
    {
        binder.readBean(this.project);
        projectService.delete(this.project.getId());
        refreshGrid();
        Notification.show("Projekt wurde gelöscht.");
        warning.close();
    }
    private void cancelDelete()
    {
        Notification.show("Löschen wurde abgebrochen.");
        warning.close();
    }

    private void createGridLayout(SplitLayout splitLayout)
    {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid()
    {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm()
    {
        populateForm(null);
        fillAssistantField();
    }

    private void populateForm(Project value)
    {
        this.project = value;
        binder.readBean(this.project);
    }

    private void fillAssistantField()
    {
        if (!authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
        {
            assistant.setValue(assistantService.get(authenticatedUser.get().get().getUsername()));
        }
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