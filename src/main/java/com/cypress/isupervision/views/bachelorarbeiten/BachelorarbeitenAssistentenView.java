/*
 * iSupervision
 * BachelorarbeitenAssistentenView
 * Bachelorstheses view for assistant users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.bachelorarbeiten;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.project.BachelorsThesis;
import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.service.AdministratorService;
import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.BachelorsThesisService;
import com.cypress.isupervision.data.service.ProjectEntityService;
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

@PageTitle("Bachelorarbeiten Assistenten")
@Route(value = "bachelorstheses/assistant/:bachelorsthesisID?/:action?(edit)", layout = MainLayout.class)
@RolesAllowed({"ASSISTANT", "ADMIN"})
public class BachelorarbeitenAssistentenView extends Div implements BeforeEnterObserver
{
    private final String BACHELORSTHESIS_ID = "bachelorsthesisID";
    private final String BACHELORSTHESIS_EDIT_ROUTE_TEMPLATE = "bachelorstheses/assistant/%s/edit";
    private final BachelorsThesisService bachelorsThesisService;
    private AssistantService assistantService;
    private AuthenticatedUser authenticatedUser;
    private Grid<BachelorsThesis> grid = new Grid<>(BachelorsThesis.class, false);
    private TextField title;
    private ComboBox<Assistant> assistant = new ComboBox<>("Assistent");
    private TextField student;
    private DatePicker deadline;
    private Checkbox finished;
    private Button cancel = new Button("Abbrechen");
    private Button save = new Button("Speichern");
    private Button delete = new Button("Löschen");
    private Button edit = new Button("Ändern");
    private BeanValidationBinder<BachelorsThesis> binder;
    private BachelorsThesis bachelorsThesis;
    private Dialog warning = new Dialog();
    private List<BachelorsThesis> bachelorsTheses;
    private int limit;

    public BachelorarbeitenAssistentenView(AuthenticatedUser authenticatedUser, BachelorsThesisService bachelorsThesisService, ProjectEntityService projectEntityService, AssistantService assistantService, AdministratorService administratorService)
    {
        this.bachelorsThesisService = bachelorsThesisService;
        this.authenticatedUser = authenticatedUser;
        this.assistantService = assistantService;
        addClassNames("bachelorarbeiten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();
        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);
        createDialog();
        add(splitLayout);

        // Configure Grid
        grid.addColumn("title").setWidth("800px");
        grid.addColumn(bachelorsThesis -> bachelorsThesis.getAssistant().getFirstname()+" "+bachelorsThesis.getAssistant().getLastname()).setHeader("Assistent").setKey("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addComponentColumn(bachelorsThesisFinished -> createFinishedIcon(bachelorsThesisFinished.isFinished())).setHeader("Abg.");

        grid.getColumnByKey("title").setHeader("Titel");
        grid.getColumnByKey("assistant").setHeader("Assistent");
        grid.getColumnByKey("student").setHeader("Student");
        grid.getColumnByKey("deadline").setHeader("Deadline");

        grid.setItems(query -> bachelorsThesisService.list(
                        PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event ->
        {
            if (event.getValue() != null)
            {
                UI.getCurrent().navigate(String.format(BACHELORSTHESIS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else
            {
                clearForm();
                UI.getCurrent().navigate(BachelorarbeitenAssistentenView.class);
            }
        });
        fillAssistantField();

        // Configure Form
        binder = new BeanValidationBinder<>(BachelorsThesis.class);

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
                this.bachelorsThesis = new BachelorsThesis();
                binder.writeBean(this.bachelorsThesis);
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
                        if (this.bachelorsThesis != null)
                        {
                            int exists = projectEntityService.exists(this.bachelorsThesis);
                            if (exists == 0)
                            {
                                bachelorsTheses=bachelorsThesisService.searchForAssistant(assistantService.get(authenticatedUser.get().get().getUsername()));
                                if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
                                {

                                    limit=administratorService.get(authenticatedUser.get().get().getUsername()).getBaLimit();
                                }
                                else
                                {
                                    limit=assistantService.get(authenticatedUser.get().get().getUsername()).getBaLimit();
                                }
                                if (bachelorsTheses.size()<limit)
                                {
                                    bachelorsThesisService.update(this.bachelorsThesis);
                                    clearForm();
                                    refreshGrid();
                                    Notification.show("Bachelorarbeit gespeichert.");
                                }
                                else
                                {
                                    Notification.show("Ihr Limit an Bachelorarbeiten ist bereits erreicht.");
                                }
                            } else
                            {
                                Notification.show("Titel existiert bereits.");
                            }
                        }
                    }
                } else
                {
                    Notification.show("Assistenten dürfen nur Bachelorarbeiten auf ihren eigenen Namen buchen.");
                }
                UI.getCurrent().navigate(BachelorarbeitenAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

        //Hook up Delete Button
        delete.addClickListener(e ->
        {
            if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || authenticatedUser.get().get().getUsername().equals(this.bachelorsThesis.getAssistant().getUsername()))
            {
                warning.open();
            }
            else
            {
                Notification.show("Assistenten dürfen nur ihre eigenen Bachelorarbeiten löschen.");
            }
        });

        //Hook up Edit Button
        edit.addClickListener(e ->
        {
            try
            {
                if (this.bachelorsThesis != null)
                {
                    if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN) || authenticatedUser.get().get().getUsername().equals(this.bachelorsThesis.getAssistant().getUsername()))
                    {
                        Assistant bachelorsThesisAssistant = this.bachelorsThesis.getAssistant();
                        binder.writeBean(this.bachelorsThesis);
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
                                bachelorsTheses=bachelorsThesisService.searchForAssistant(assistantService.get(authenticatedUser.get().get().getUsername()));
                                if (authenticatedUser.get().get().getRoles().contains(Role.ADMIN))
                                {

                                    limit=administratorService.get(authenticatedUser.get().get().getUsername()).getBaLimit();
                                }
                                else
                                {
                                    limit = assistantService.get(authenticatedUser.get().get().getUsername()).getBaLimit();
                                }
                                if (bachelorsTheses.size()<limit || bachelorsThesisAssistant.getUsername().equals(authenticatedUser.get().get().getUsername()))
                                {
                                    bachelorsThesisService.update(this.bachelorsThesis);
                                    clearForm();
                                    refreshGrid();
                                    Notification.show("Bachelorarbeit wurde bearbeitet.");
                                }
                                else
                                {
                                    Notification.show("Ihr Limit an Bachelorarbeiten ist bereits erreicht.");
                                }
                            }
                        } else
                        {
                            Notification.show("Assistenten dürfen nur Bachelorarbeiten auf ihren eigenen Namen buchen.");
                        }
                    } else
                    {
                        Notification.show("Assistenten dürfen nur ihre eigenen Bachelorarbeiten verändern.");
                    }
                } else
                {
                    Notification.show("Keine Bachelorarbeit ausgewählt.");
                }
                UI.getCurrent().navigate(BachelorarbeitenAssistentenView.class);
            } catch (ValidationException validationException)
            {
                Notification.show("Es ist leider etwas schief gegangen.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        Optional<UUID> bachelorThesisId = event.getRouteParameters().get(BACHELORSTHESIS_ID).map(UUID::fromString);
        if (bachelorThesisId.isPresent())
        {
            Optional<BachelorsThesis> bachelorsThesisFromBackend = bachelorsThesisService.get(bachelorThesisId.get());
            if (bachelorsThesisFromBackend.isPresent())
            {
                populateForm(bachelorsThesisFromBackend.get());
            } else
            {
                Notification.show(String.format("Bachelorarbeit wurde nicht gefunden, ID = %s", bachelorThesisId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(BachelorarbeitenAssistentenView.class);
            }
        }
    }

    private void createAssistantBox()
    {
        assistant.setAllowCustomValue(false);
        assistant.setPlaceholder("Assistenten auswählen");
        List<Assistant> assistants = assistantService.getAll();
        assistant.setItems(assistants);
        assistant.setItemLabelGenerator(person -> person.getFirstname() + " " + person.getLastname());
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
        student = new TextField("Student");
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
        warning.add(new Paragraph("Sind Sie sicher, dass Sie diese Bachelorarbeit löschen möchten?"));
        Button delete = new Button("Löschen");
        Button cancel = new Button("Abbrechen");
        warning.add(delete, cancel);
        delete.addClickListener(event -> {confirmDelete();});
        cancel.addClickListener(event->{cancelDelete();});
    }
    private void confirmDelete()
    {
        binder.readBean(this.bachelorsThesis);
        bachelorsThesisService.delete(this.bachelorsThesis.getId());
        refreshGrid();
        Notification.show("Bachelorarbeit wurde gelöscht.");
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

    private void populateForm(BachelorsThesis value)
    {
        this.bachelorsThesis = value;
        binder.readBean(this.bachelorsThesis);
    }

    private void fillAssistantField()
    {
        if (!authenticatedUser.get().get().getRoles().toString().contains("ADMIN"))
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