package com.cypress.isupervision.views.meineprojekte;

import com.cypress.isupervision.data.entity.project.MastersThesis;
import com.cypress.isupervision.data.service.MastersThesisService;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Meine Projekte")
@Route(value = "myprojects/:mastersThesisID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class MeineProjekteView extends Div implements BeforeEnterObserver {

    private final String MASTERSTHESIS_ID = "mastersThesisID";
    private final String MASTERSTHESIS_EDIT_ROUTE_TEMPLATE = "myprojects/%s/edit";

    private Grid<MastersThesis> grid = new Grid<>(MastersThesis.class, false);

    private TextField title;
    private TextField assistant;
    private TextField student;
    private DatePicker deadline;
    private DatePicker examDate;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<MastersThesis> binder;

    private MastersThesis mastersThesis;

    private final MastersThesisService mastersThesisService;

    @Autowired
    public MeineProjekteView(MastersThesisService mastersThesisService) {
        this.mastersThesisService = mastersThesisService;
        addClassNames("meine-projekte-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("title").setAutoWidth(true);
        grid.addColumn("assistant").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addColumn("examDate").setAutoWidth(true);
        grid.setItems(query -> mastersThesisService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MASTERSTHESIS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MeineProjekteView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(MastersThesis.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.mastersThesis == null) {
                    this.mastersThesis = new MastersThesis();
                }
                binder.writeBean(this.mastersThesis);

                mastersThesisService.update(this.mastersThesis);
                clearForm();
                refreshGrid();
                Notification.show("Masterarbeit details stored.");
                UI.getCurrent().navigate(MeineProjekteView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the masterarbeit details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> masterarbeitId = event.getRouteParameters().get(MASTERSTHESIS_ID).map(UUID::fromString);
        if (masterarbeitId.isPresent()) {
            Optional<MastersThesis> mastersThesisFromBackend = mastersThesisService.get(masterarbeitId.get());
            if (mastersThesisFromBackend.isPresent()) {
                populateForm(mastersThesisFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested masterarbeit was not found, ID = %s", masterarbeitId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MeineProjekteView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
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
        examDate = new DatePicker("Pruefungstermin");
        Component[] fields = new Component[]{title, assistant, student, deadline, examDate};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(MastersThesis value) {
        this.mastersThesis = value;
        binder.readBean(this.mastersThesis);

    }
}
