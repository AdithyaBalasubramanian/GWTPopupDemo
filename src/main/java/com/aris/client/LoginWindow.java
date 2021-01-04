package com.aris.client;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;

public class LoginWindow extends Composite {
    @UiTemplate("LoginWindow.ui.xml")
    interface  Binder extends UiBinder<DockLayoutPanel, LoginWindow> {}
    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    TextBox userName;

    @UiField
    PasswordTextBox password;

    @UiField
    Button submit;

}
