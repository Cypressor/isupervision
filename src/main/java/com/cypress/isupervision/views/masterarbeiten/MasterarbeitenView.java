package com.cypress.isupervision.views.masterarbeiten;

import com.cypress.isupervision.data.entity.Masterarbeit;
import com.cypress.isupervision.data.service.MasterarbeitService;
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

@PageTitle("Masterarbeiten")
@Route(value = "masterarbeiten/:masterarbeitID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class MasterarbeitenView extends Div implements BeforeEnterObserver {

    private final String MASTERARBEIT_ID = "masterarbeitID";
    private final String MASTERARBEIT_EDIT_ROUTE_TEMPLATE = "masterarbeiten/%s/edit";

    private Grid<Masterarbeit> grid = new Grid<>(Masterarbeit.class, false);

    private TextField titel;
    private TextField assistent;
    private TextField student;
    private DatePicker deadline;
    private DatePicker pruefungstermin;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<Masterarbeit> binder;

    private Masterarbeit masterarbeit;

    private final MasterarbeitService masterarbeitService;

    @Autowired
    public MasterarbeitenView(MasterarbeitService masterarbeitService) {
        this.masterarbeitService = masterarbeitService;
        addClassNames("masterarbeiten-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("titel").setAutoWidth(true);
        grid.addColumn("assistent").setAutoWidth(true);
        grid.addColumn("student").setAutoWidth(true);
        grid.addColumn("deadline").setAutoWidth(true);
        grid.addColumn("pruefungstermin").setAutoWidth(true);
        grid.setItems(query -> masterarbeitService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(MASTERARBEIT_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(MasterarbeitenView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Masterarbeit.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.masterarbeit == null) {
                    this.masterarbeit = new Masterarbeit();
                }
                binder.writeBean(this.masterarbeit);

                masterarbeitService.update(this.masterarbeit);
                clearForm();
                refreshGrid();
                Notification.show("Masterarbeit details stored.");
                UI.getCurrent().navigate(MasterarbeitenView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the masterarbeit details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> masterarbeitId = event.getRouteParameters().get(MASTERARBEIT_ID).map(UUID::fromString);
        if (masterarbeitId.isPresent()) {
            Optional<Masterarbeit> masterarbeitFromBackend = masterarbeitService.get(masterarbeitId.get());
            if (masterarbeitFromBackend.isPresent()) {
                populateForm(masterarbeitFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested masterarbeit was not found, ID = %s", masterarbeitId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(MasterarbeitenView.class);
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
        titel = new TextField("Titel");
        assistent = new TextField("Assistent");
        student = new TextField("Student");
        deadline = new DatePicker("Deadline");
        pruefungstermin = new DatePicker("Pruefungstermin");
        Component[] fields = new Component[]{titel, assistent, student, deadline, pruefungstermin};

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

    private void populateForm(Masterarbeit value) {
        this.masterarbeit = value;
        binder.readBean(this.masterarbeit);

    }
}
