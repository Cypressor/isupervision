package com.cypress.isupervision.views.account;

import com.cypress.isupervision.data.Role;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.security.PermitAll;
import java.util.Set;

@PageTitle("Account")
@Route(value = "account", layout = MainLayout.class)
@PermitAll
@RouteAlias(value="", layout = MainLayout.class)
public class AccountView extends VerticalLayout implements BeforeEnterObserver
{

    private String role="";

private AuthenticatedUser authenticatedUser;

    @Autowired
    public AccountView(AuthenticatedUser authenticatedUser) {

        this.authenticatedUser=authenticatedUser;
        getUserRole();

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {
        getUserRole();
        redirect(event);

    }
    private void getUserRole()
    {
        Set<Role> roles = authenticatedUser.get().get().getRoles();
        if (roles.toString().contains("ADMIN"))
        {
            role="Admin";
        }
        else
        {
            if(roles.toString().contains("ASSISTANT"))
            {
                role="Assistent";
            }
            else if (roles.toString().contains("STUDENT"))
            {
                role="Student";
            }
        }

    }
    private void redirect(BeforeEnterEvent event)
    {
        if (this.role.equals("Admin"))
        {
            event.rerouteTo(AccountAdminView.class);
        }
        if (this.role.equals("Student"))
        {
            event.rerouteTo(AccountStudentenView.class);
        }
        if (role.equals("Assistent"))
        {
            event.rerouteTo(AccountAssistentenView.class);
        }
    }

}
