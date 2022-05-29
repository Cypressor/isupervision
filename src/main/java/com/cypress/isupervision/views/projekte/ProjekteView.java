package com.cypress.isupervision.views.projekte;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.data.entity.project.Project;
import com.cypress.isupervision.data.service.ProjectService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.cypress.isupervision.views.account.AccountAdminView;
import com.cypress.isupervision.views.account.AccountAssistentenView;
import com.cypress.isupervision.views.account.AccountStudentenView;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Projekte")
@Route(value = "projects/:projectID?/:action?(edit)", layout = MainLayout.class)
@PermitAll
public class ProjekteView extends VerticalLayout implements BeforeEnterObserver {

    private String role="";

    private AuthenticatedUser authenticatedUser;

    @Autowired
    public ProjekteView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser=authenticatedUser;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        redirect(event);
    }

    private void redirect(BeforeEnterEvent event)
    {
        Set<Role> roles = authenticatedUser.get().get().getRoles();
        if (roles.toString().contains("ADMIN") || roles.toString().contains("ASSISTANT"))
        {
            event.rerouteTo(ProjekteAssistentenView.class);
        }
        else if (roles.toString().contains("STUDENT"))
        {
            event.rerouteTo(ProjekteStudentenView.class);
        }

    }
}
