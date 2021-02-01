package com.aris.client;

import com.aris.shared.Person;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class UserDetailsPresenter {

    public interface Display {

        ControlLabel getUserNameLabel();

        ControlLabel getFirstNameLabel();

        ControlLabel getLastNameLabel();

        ControlGroup getUserDetails();

        Widget asWidget();
    }

    private final Display display;

    public UserDetailsPresenter() {
        this.display = new UserDetailsView();
    }

    public void go(final HasWidgets container, final Person person) {
        container.clear();
        displayUser(person);
        container.add(display.asWidget());
    }

    public void displayUser(Person person) {
        display.getUserDetails().setStyleName("details");
        display.getUserNameLabel().getElement().setInnerHTML("User ID = " + person.getUserId());
        display.getFirstNameLabel().getElement().setInnerHTML("First Name = " + person.getFirstName());
        display.getLastNameLabel().getElement().setInnerHTML("Last Name = " + person.getLastName());
    }
}
