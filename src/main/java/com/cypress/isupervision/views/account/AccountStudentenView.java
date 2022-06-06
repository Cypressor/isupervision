/*
 * iSupervision
 * AccountStudentenView
 * AccountView for student users
 * Author: Martin Lunelli
 * Last Change: 04.06.2022
 */

package com.cypress.isupervision.views.account;

import com.cypress.isupervision.data.entity.user.Student;
import com.cypress.isupervision.data.service.AssistantService;
import com.cypress.isupervision.data.service.StudentService;
import com.cypress.isupervision.security.AuthenticatedUser;
import com.cypress.isupervision.views.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.RolesAllowed;

@PageTitle("Account Studenten")
@Route(value = "account/student", layout = MainLayout.class)
@RolesAllowed("STUDENT")
public class AccountStudentenView extends VerticalLayout
{
    private Student student;
    public AccountStudentenView(StudentService studentService, AuthenticatedUser authenticatedUser)
    {
        student = studentService.get(authenticatedUser.get().get().getUsername());
        H1 greeting = new H1("Hello" + " " + student.getFirstname() + " " + student.getLastname());
        VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        fieldLayout.add(greeting);
        add(fieldLayout);
    }
}