package com.cypress.isupervision.views.masterarbeiten;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.service.AdministratorService;
import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.MastersThesisService;
import com.cypress.isupervision.data.service.ProjectEntityService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
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

@PageTitle("Masterarbeiten Assistenten")
@Route(value = "mastersthesis/assistant/:mastersthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ASSISTANT", "ADMIN"})
public class MasterarbeitenAssistentenView extends Div implements BeforeEnterObserver
{

    private final String MASTERSTHESIS_ID = "mastersthesisID";
    private final String MASTERSTHESIS_EDIT_ROUTE_TEMPLATE = "mastersthesis/assistant/%s/edit";
    private final MastersThesisService mastersThesisService;
    private AuthenticatedUser authenticatedUser;
    private Grid<MastersThesis> grid = new Grid<>(MastersThesis.class, false);
    private TextField title;
    private TextField assistant;
    private TextField student;
    private DatePicker deadline;
    private DatePicker examDate;
    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button edit = new Button("Ändern");
    private BeanValidationBinder<MastersThesis> binder;
    private MastersThesis mastersThesis;
    private Dialog warning = new Dialog();
    private List<MastersThesis> mastersTheses;
    private int limit;

    public MasterarbeitenAssistentenView(AuthenticatedUser authenticatedUser, MastersThesisService mastersThesisService, ProjectEntityService projectEntityService, AssistantService assistantService, AdministratorService administratorService)
    {
        this.authenticatedUser = authenticatedUser;
        this.mastersThesisService = mastersThesisService;
        addClassNames("masterarbeiten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);

        // Configure Grid
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

        grid.setItems(query -> mastersThesisService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event ->
        {
            if (event.getValue() != null)
            {
                UI.getCurrent().navigate(String.format(MASTERSTHESIS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else
            {
                clearForm();
                UI.getCurrent().navigate(MasterarbeitenAssistentenView.class);
            }
        });
        fillAssistantField();

        // Configure Form
        binder = new BeanValidationBinder<>(MastersThesis.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.bindInstanceFields(this);

        //Hook up Cancel Button
        cancel.addClickListener(e ->
        {
            clearForm();
            refreshGrid();
        });

        //Hook up Save Button
        save.addClickListener(e ->
        {
            try
            {
                this.mastersThesis = new MastersThesis();
                binder.writeBean(this.mastersThesis);

                if (title.getValue().trim().equals(""))
                {
                    Notification.show("Bitte Titel eintragen.");
                } else
                {
                    if (assistant.getValue().trim().equals(""))
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
                if (authenticatedUser.get().get().getRoles().toString().contains("ADMIN") || (authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname()).equals(assistant.getValue()))
                {
                    if (!title.getValue().trim().equals("") && !assistant.getValue().trim().equals("") && !deadline.isEmpty())
                    {
                        if (this.mastersThesis != null)
                        {
                            int exists = projectEntityService.exists(this.mastersThesis);
                            if (exists == 0)
                            {
                                mastersTheses=mastersThesisService.searchForAssistant(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
                                if (authenticatedUser.get().get().getRoles().toString().contains("ADMIN"))
                                {

                                    limit=administratorService.get(authenticatedUser.get().get().getUsername()).getMaLimit();
                                }
                                else
                                {
                                    limit=assistantService.get(authenticatedUser.get().get().getUsername()).getMaLimit();
                                }
                                if (mastersTheses.size()<limit)
                                {
                                mastersThesisService.update(this.mastersThesis);
                                clearForm();
                                refreshGrid();
                                Notification.show("Masterarbeit gespeichert.");
                                }
                                else
                                {
                                    Notification.show("Ihr Limit an Masterarbeiten ist bereits erreicht.");
                                }
                            } else
                            {
                                Notification.show("Titel existiert bereits.");
                            }
                        }
                    }
                } else
                {
                    Notification.show("Assistenten dürfen nur Masterarbeiten auf ihren eigenen Namen buchen.");
                }
                UI.getCurrent().navigate(MasterarbeitenAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

        //Hook up Delete Button
        delete.addClickListener(e ->
        {
            if (authenticatedUser.get().get().getRoles().toString().contains("ADMIN") || (authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname()).equals(this.mastersThesis.getAssistant()))
            {
                warning.open();
            }
            else
                {
                    Notification.show("Assistenten dürfen ihre eigenen Masterarbeiten löschen.");
                }
        });

        //Hook up Edit Button
        edit.addClickListener(e ->
        {
            try
            {
                if (this.mastersThesis != null)
                {
                    if (authenticatedUser.get().get().getRoles().toString().contains("ADMIN") || (authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname()).equals(this.mastersThesis.getAssistant()))
                    {
                        binder.writeBean(this.mastersThesis);
                        if (title.getValue().trim().equals(""))
                        {
                            Notification.show("Bitte Titel eintragen.");
                        } else
                        {
                            if (assistant.getValue().trim().equals(""))
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
                        if (authenticatedUser.get().get().getRoles().toString().contains("ADMIN") || (authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname()).equals(assistant.getValue()))
                        {
                            if (!title.getValue().trim().equals("") && !assistant.getValue().trim().equals("") && !deadline.isEmpty())
                            {
                                mastersThesisService.update(this.mastersThesis);
                                clearForm();
                                refreshGrid();
                                Notification.show("Masterarbeit wurde bearbeitet.");
                            }
                        } else
                        {
                            Notification.show("Assistenten dürfen nur Masterarbeiten auf ihren eigenen Namen buchen.");
                        }
                    } else
                    {
                        Notification.show("Assistenten dürfen nur ihre eigenen Masterarbeiten verändern.");
                    }
                } else
                {
                    Notification.show("Keine Masterarbeit ausgewählt.");
                }
                UI.getCurrent().navigate(MasterarbeitenAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        Optional<UUID> mastersThesisId = event.getRouteParameters().get(MASTERSTHESIS_ID).map(UUID::fromString);
        if (mastersThesisId.isPresent())
        {
            Optional<MastersThesis> mastersThesisFromBackend = mastersThesisService.get(mastersThesisId.get());
            if (mastersThesisFromBackend.isPresent())
            {
                populateForm(mastersThesisFromBackend.get());
            } else
            {
                Notification.show(String.format("Masterarbeit wurde nicht gefunden, ID = %s", mastersThesisId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterarbeitenAssistentenView.class);
            }
        }
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
        assistant = new TextField("Assistent");
        student = new TextField("Student");
        deadline = new DatePicker("Deadline");
        examDate = new DatePicker("examDate");
        Component[] fields = new Component[]{title, assistant, student, deadline, examDate};

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
        warning.add(new Paragraph("Sind Sie sicher, dass Sie diese Masterarbeit löschen möchten?"));
        Button delete = new Button("Löschen");
        Button cancel = new Button("Abbrechen");
        warning.add(delete, cancel);
        delete.addClickListener(event -> {confirmDelete();});
        cancel.addClickListener(event->{cancelDelete();});
    }
    private void confirmDelete()
    {
        binder.readBean(this.mastersThesis);
        mastersThesisService.delete(this.mastersThesis.getId());
        refreshGrid();
        Notification.show("Masterarbeit wurde gelöscht.");
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

    private void populateForm(MastersThesis value)
    {
        this.mastersThesis = value;
        binder.readBean(this.mastersThesis);
    }

    private void fillAssistantField()
    {
        if (!authenticatedUser.get().get().getRoles().toString().contains("ADMIN"))
        {
            assistant.setValue(authenticatedUser.get().get().getFirstname() + " " + authenticatedUser.get().get().getLastname());
        }
    }
}
