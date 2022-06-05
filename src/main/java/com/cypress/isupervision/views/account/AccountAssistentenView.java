/*
 * iSupervision
 * AccountAssistentenView
 * AccountView for assistant users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.account;

import com.cypress.isupervision.data.entity.user.Assistant;
import com.cypress.isupervision.data.service.AssistantService;
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

@PageTitle("Account Assistenten")
@Route(value = "account/assistant", layout = MainLayout.class)
@RolesAllowed("ASSISTANT")
public class AccountAssistentenView extends VerticalLayout
{
    private TextField projLimit = new TextField("Projekt-Limit");
    private TextField baLimit = new TextField("Bachelorarbeit-Limit");
    private TextField maLimit = new TextField("Masterarbeit-Limit");
    private Button saveButton = new Button("Save All");
    private Assistant assistant;

    @Autowired
    AccountAssistentenView(AuthenticatedUser authenticatedUser, AssistantService assistantService)
    {
        assistant = assistantService.get(authenticatedUser.get().get().getUsername());
        createLayout();
        saveButton.addClickListener(event ->
        {
            assistant.setProjLimit(Integer.parseInt(projLimit.getValue()));
            assistant.setBaLimit(Integer.parseInt(baLimit.getValue()));
            assistant.setMaLimit(Integer.parseInt(maLimit.getValue()));
            assistantService.update(assistant);
            Notification.show("Änderungen an den Limits gespeichert.");
        });
    }

    private void createLayout()
    {
        VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.add(projLimit,baLimit,maLimit,saveButton);
        projLimit.setValue(assistant.getProjLimit().toString());
        baLimit.setValue(assistant.getBaLimit().toString());
        maLimit.setValue(assistant.getMaLimit().toString());
        add(fieldLayout);
    }
}
