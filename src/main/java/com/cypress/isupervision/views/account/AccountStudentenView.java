package com.cypress.isupervision.views.account;

import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.security.RolesAllowed;

@PageTitle("Account Studenten")
@Route(value = "account/student", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class AccountStudentenView extends VerticalLayout
{

    public AccountStudentenView()
    {

    }
}