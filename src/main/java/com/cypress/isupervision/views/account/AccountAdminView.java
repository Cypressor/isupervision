/*
 * iSupervision
 * AccountAdminView
 * AccountView for admin users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.account;

import com.cypress.isupervision.data.entity.user.Administrator;
import com.cypress.isupervision.data.service.AdministratorService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.security.RolesAllowed;

@PageTitle("Account Admin")
@Route(value = "account/admin", layout = MainLayout.class)
@RolesAllowed("ADMIN")
public class AccountAdminView extends VerticalLayout
{
    private TextField projLimit = new TextField("Projekt-Limit");
    private TextField baLimit = new TextField("Bachelorarbeit-Limit");
    private TextField maLimit = new TextField("Masterarbeit-Limit");
    private Button saveButton = new Button("Save All");
    private Administrator admin;

    @Autowired
    AccountAdminView(AuthenticatedUser authenticatedUser, AdministratorService administratorService)
    {
        admin = administratorService.get(authenticatedUser.get().get().getUsername());
        createLayout();

        saveButton.addClickListener(event ->
        {
            try
            {
                admin.setProjLimit(Integer.parseInt(projLimit.getValue()));
                admin.setBaLimit(Integer.parseInt(baLimit.getValue()));
                admin.setMaLimit(Integer.parseInt(maLimit.getValue()));
                administratorService.update(admin);
                Notification.show("Änderungen an den Limits gespeichert.");
            }
            catch(Exception e)
            {
                Notification.show("Bitte nur Zahlen eintragen.");
            }
        });
    }

    private void createLayout()
    {
        VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.add(projLimit,baLimit,maLimit,saveButton);
        projLimit.setValue(admin.getProjLimit().toString());
        baLimit.setValue(admin.getBaLimit().toString());
        maLimit.setValue(admin.getMaLimit().toString());
        add(fieldLayout);
    }
}