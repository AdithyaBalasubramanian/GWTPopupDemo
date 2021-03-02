package com.aris.admin.client;


import com.aris.admin.shared.WindowProps;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;



public class LoginDetailsPresenter {

    public interface Display {

        ControlLabel getTitleCard();

        ControlGroup getLogInForm();

        ControlLabel getUserNameLabel();

        TextBox getUserNameTextBox();

        ControlLabel getHostNameLabel();

        TextBox getUserHostNameTextBox();

        ControlLabel getGrantTypeLabel();

        TextBox getGrantTypeTextBox();

        ControlLabel getClientIdLabel();

        TextBox getClientIdTextBox();

        ControlLabel getTenantLabel();

        TextBox getTenantTextBox();

        ControlLabel getLocaleLabel();

        TextBox getLocaleTextBox();

        ControlLabel getRedirectUrlLabel();

        TextBox getRedirectUrlTextBox();

        ControlLabel getPasswordLabel();

        PasswordTextBox getPasswordTextBox();

        Button getSignInButton();

        Button getLoginWithArisButton();

        Widget asWidget();

    }

    private Display display;

    public LoginDetailsPresenter() {

        this.display = new LoginDetailsView();
        display.getTitleCard().getElement().setInnerHTML("Test Client");
        display.getTitleCard().setStyleName("title");

        display.getLogInForm().setStyleName("details");
        display.getHostNameLabel().getElement().setInnerHTML("Enter the hostName of the Auth Server: ");
        display.getGrantTypeLabel().getElement().setInnerHTML("Enter the grant Type of the application: ");
        display.getClientIdLabel().getElement().setInnerHTML("Enter the client Id of the application: ");
        display.getTenantLabel().getElement().setInnerHTML("Enter the tenant Name: ");
        display.getTenantTextBox().setText("default");
        display.getLocaleLabel().getElement().setInnerHTML("Enter the locale: ");
        display.getLocaleTextBox().setText("English");
        display.getRedirectUrlLabel().getElement().setInnerHTML("Enter the redirectUrl for the client application: ");

        display.getUserNameLabel().getElement().setInnerHTML("Enter your username: ");
        display.getUserNameTextBox().setText("system");
        display.getPasswordLabel().getElement().setInnerHTML("Enter your password: ");
        display.getSignInButton().setText("Sign in");
        display.getSignInButton().setStyleName("send-button");
        display.getLoginWithArisButton().setText("Login with ARIS");
        display.getLoginWithArisButton().setStyleName("aris-button");


        display.getLoginWithArisButton().addClickHandler(clickEvent -> {
            //TODO part of the UI
            String hostName = display.getUserHostNameTextBox().getText();
            String grantType = display.getGrantTypeTextBox().getText();
            String clientId = display.getClientIdTextBox().getText();
            String tenant = display.getTenantTextBox().getText();
            String locale = display.getLocaleTextBox().getText();
            String redirectUrl = display.getRedirectUrlTextBox().getText();
            String landingPage = getLandingPageUrl();
            String url = constructLoginPageUrl(hostName, grantType, clientId, tenant, locale, redirectUrl);
            openAndCloseWindow(url, "_blank", landingPage, new WindowProps());
        });

    }

    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
    }

    public native static String getLandingPageUrl() /*-{
        return $wnd.location.href;
    }-*/;

    public native static String constructLoginPageUrl (String hostName, String grantType, String clientId, String tenant, String locale, String redirectUrl) /*-{
        return $wnd.constructUrl(hostName, tenant, locale, clientId, grantType, redirectUrl);
    }-*/;

    public native static void openAndCloseWindow (String url, String target, String landingPage, WindowProps windowProps) /*-{
        var childWindow = $wnd.openWindow(url, target, windowProps);
        $wnd.closeWindowOnRedirect(childWindow, landingPage);
    }-*/;
}
