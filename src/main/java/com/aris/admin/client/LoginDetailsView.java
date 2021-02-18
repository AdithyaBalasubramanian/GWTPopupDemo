package com.aris.admin.client;

import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;

public class LoginDetailsView extends Composite implements LoginDetailsPresenter.Display {

    @UiTemplate("LoginDetailsView.ui.xml")
    interface Binder extends UiBinder<DockLayoutPanel, LoginDetailsView> {}
    private static final LoginDetailsView.Binder binder = GWT.create(LoginDetailsView.Binder.class);

    @UiField
    ControlLabel titleCard;

    @UiField
    ControlGroup loginForm;

    @UiField
    TextBox userNameTextBox;

    @UiField
    ControlLabel userNameLabel;

    @UiField
    ControlLabel passwordLabel;

    @UiField
    PasswordTextBox passwordTextBox;

    @UiField
    Button loginWithArisButton;

    @UiField
    Button signInButton;

    /*@UiField
    Button userDetailsPageButton;*/

    @Override
    public ControlLabel getTitleCard() {
        return titleCard;
    }

    @Override
    public ControlGroup getLogInForm() {
        return loginForm;
    }

    @Override
    public TextBox getUserNameTextBox() {
        return userNameTextBox;
    }

    @Override
    public PasswordTextBox getPasswordTextBox() {
        return passwordTextBox;
    }

    @Override
    public com.google.gwt.user.client.ui.Button getSignInButton() {
        return signInButton;
    }

    @Override
    public com.google.gwt.user.client.ui.Button getLoginWithArisButton() {
        return loginWithArisButton;
    }

   /* @Override
    public com.google.gwt.user.client.ui.Button getUserDetailsPageButton() {
        return userDetailsPageButton;
    }
*/
    @Override
    public ControlLabel getUserNameLabel() {
        return userNameLabel;
    }

    @Override
    public ControlLabel getPasswordLabel() {
        return passwordLabel;
    }

    public LoginDetailsView() {
        DockLayoutPanel panel = binder.createAndBindUi(this);
        panel.setStyleName("content");
        initWidget(panel);
    }
}
