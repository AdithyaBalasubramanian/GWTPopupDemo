package com.aris.admin.client;


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
        display.getUserNameLabel().getElement().setInnerHTML("Enter your username: ");
        display.getUserNameTextBox().getElement().setInnerHTML("system");
        display.getPasswordLabel().getElement().setInnerHTML("Enter your password: ");
        display.getSignInButton().setText("Sign in");
        display.getSignInButton().setStyleName("send-button");
        display.getLoginWithArisButton().setText("Login with ARIS");
        display.getLoginWithArisButton().setStyleName("aris-button");


        display.getLoginWithArisButton().addClickHandler(clickEvent -> {
            String hostName = "http://10.248.91.205/umc/oauthLogin";
            String grantType = "authorization_code";
            String clientId = "2de8e473-6e51-438c-aafa-6ae3e0c94ea5";
            String tenant = "default";
            String locale = "English";
            String redirectUrl = "http://localhost:8080/umc/redirect";
            String userInfoUrl = "http://localhost:8080/umc/userinfo1";
            String url = constructLoginPageUrl(hostName, grantType, clientId, tenant, locale, redirectUrl);
            openAndCloseWindow(url, "_blank", redirectUrl, userInfoUrl);
        });

    }

    public void go(HasWidgets container) {
        container.clear();
        container.add(display.asWidget());
    }

    public native static String constructLoginPageUrl (String hostName, String grantType, String clientId, String tenant, String locale, String redirectUrl) /*-{
        return $wnd.constructUrl(hostName, tenant, locale, clientId, grantType, redirectUrl);
    }-*/;

    public native static void openAndCloseWindow (String url, String target, String redirectUrl, String userInfoUrl) /*-{
        var childWindow = $wnd.openWindow(url, target);
        $wnd.closeWindowOnRedirect(childWindow, redirectUrl, userInfoUrl);
    }-*/;
}
