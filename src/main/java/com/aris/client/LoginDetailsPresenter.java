package com.aris.client;

import com.aris.shared.Person;
import com.github.gwtbootstrap.client.ui.ControlGroup;
import com.github.gwtbootstrap.client.ui.ControlLabel;
import com.github.gwtbootstrap.client.ui.PasswordTextBox;
import com.github.gwtbootstrap.client.ui.TextBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;

import static com.aris.shared.Utils.consoleLog;

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

       /* Button getUserDetailsPageButton();*/

        Widget asWidget();

    }

    private Display display;

    public LoginDetailsPresenter() {
        UserDetailsPresenter userDetailsPresenter = new UserDetailsPresenter();
        this.display = new LoginDetailsView();
        display.getTitleCard().getElement().setInnerHTML("Process Mining client");
        display.getTitleCard().setStyleName("title");

        display.getLogInForm().setStyleName("details");
        display.getUserNameLabel().getElement().setInnerHTML("Enter your username: ");
        display.getUserNameTextBox().getElement().setInnerHTML("system");
        display.getPasswordLabel().getElement().setInnerHTML("Enter your password: ");
        display.getSignInButton().setText("Sign in");
        display.getSignInButton().setStyleName("send-button");
        display.getLoginWithArisButton().setText("Login with ARIS");
        display.getLoginWithArisButton().setStyleName("aris-button");
        /*display.getUserDetailsPageButton().getElement().setInnerHTML("User Details");
        display.getUserDetailsPageButton().setStyleName("send-button");*/

        display.getLoginWithArisButton().addClickHandler(clickEvent -> {
            consoleLog("Click handler invoked");
            String winUrl = "http://sbrapp10srv.eur.ad.sag/umc";
            //Need to get the new UMC login page that has been developed
            winUrl = "10.248.91.205";
            //openNewWindow(winUrl);
            consoleLog("Invoking callback URI");
            Model.SERVICE.redirectURI("authCode", winUrl, new AsyncCallback<Person>() {
                @Override
                public void onFailure(Throwable throwable) {
                    consoleLog(Arrays.toString(throwable.getStackTrace()));
                }

                @Override
                public void onSuccess(Person person) {
                    consoleLog("Successful callback...");
                    RootLayoutPanel container = RootLayoutPanel.get();
                    container.clear();
                    userDetailsPresenter.go(container, person);
                }
            });
            //For the callBackURI, need to send the well known endpoints URL
            //callbackURI("AuthCode", winUrl);
        });

       /* display.getUserDetailsPageButton().addClickHandler(clickEvent -> {
            RootLayoutPanel container = RootLayoutPanel.get();
            container.clear();
            userDetailsPresenter.go(container, new Person("sampleUserId", "sampleFN", "sampleLN"));
        });*/
    }



    public void go(HasWidgets container) {
        container.clear();

        container.add(display.asWidget());
    }

    private void openNewWindow(String url) {
        String params = "menubar=no,location=false,resizable=yes,scrollbars=yes,status=no,dependent=true,width=1200,height=720";
        Window.open(url, "_blank", params);

        Window.addWindowClosingHandler(closeEvent -> {
            consoleLog("Window closed" + closeEvent.toString());
        });

        //After opening this window, we need to get the callback to the redirect uri
        //which is hosted by the redirect servlet
        //the servlet will have an HTTPClient to make further calls to the Auth Server
        //to get the Auth Code, and AccessToken + UserInfo
    }



    private void callbackURI(String authCode, String hostName) {
        /*
        String authCode = nameField.getText();
        if (!Verifier.isValid(authCode)) {
          errorLabel.setText("Please provide valid Auth Code");
          return;
        }
        */

        //How to get this servlet exposed to the outside world ?!
        Model.SERVICE.redirectURI(authCode, hostName, new AsyncCallback<Person>() {
            public void onFailure(Throwable caught) {
                // Show the RPC error message to the user
            }

            public void onSuccess(Person result) {
                //presenter.go(RootPanel.get());
            }
        });
    }
}
