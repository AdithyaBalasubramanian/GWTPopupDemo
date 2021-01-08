package com.aris.client;

import com.aris.shared.Verifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class LoginEntryPoint implements EntryPoint {
  /**
   * The message displayed to the user when the server cannot be reached or
   * returns an error.
   */
  private static final String SERVER_ERROR = "An error occurred while "
      + "attempting to contact the server. Please check your network "
      + "connection and try again.";

  /**
   * Create a remote service proxy to talk to the server-side Greeting service.
   */
  private final IProcessMiningServletAsync miningService = GWT.create(IProcessMiningServlet.class);

  /**
   * This is the entry point method.
   */
  public void onModuleLoad() {

    final UserDetailsPresenter presenter = new UserDetailsPresenter();

    final Button logInWithAris = new Button("Log in with ARIS");
    final Button sendButton = new Button("Sign in");
    final TextBox nameField = new TextBox();
    final PasswordTextBox passwdField = new PasswordTextBox();
    nameField.setText("system");
    final Label errorLabel = new Label();

    // We can add style names to widgets
    sendButton.addStyleName("sendButton");
    logInWithAris.addStyleName("aris-button");

    // Add the nameField and sendButton to the RootPanel
    // Use RootPanel.get() to get the entire body element
    RootPanel.get("nameFieldContainer").add(nameField);
    RootPanel.get("passwdFieldContainer").add(passwdField);
    RootPanel.get("sendButtonContainer").add(sendButton);
    RootPanel.get("loginButtonContainer").add(logInWithAris);
    RootPanel.get("errorLabelContainer").add(errorLabel);

    // Focus the cursor on the name field when the app loads
    nameField.setFocus(true);
    nameField.selectAll();

    // Create a handler for the sendButton and nameField
    class MyHandler implements ClickHandler, KeyUpHandler {
      /**
       * Fired when the user clicks on the sendButton.
       */
      public void onClick(ClickEvent event) {
        String winUrl = GWT.getHostPageBaseURL().replace("landing", "login-popup");
        winUrl = "http://sbrapp10srv.eur.ad.sag/";
        String winName = "Testing Window";
        openNewWindow(winUrl);
        presenter.go(RootPanel.get());
        // sendNameToServer();
      }

      /**
       * Fired when the user types in the nameField.
       */
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          //sendNameToServer();
        }
      }

      /**
       * Callback to the method which will provide the usersDetails
       */
      private void sendNameToServer() {

        errorLabel.setText("");
        //Need to get the auth code correctly
        String authCode = nameField.getText();
        if (!Verifier.isValid(authCode)) {
          errorLabel.setText("Please provide valid Auth Code");
          return;
        }

        logInWithAris.setEnabled(false);

        //How to get this servlet exposed to the outside world ?!
        miningService.redirectURI(authCode, new AsyncCallback<String>() {
          public void onFailure(Throwable caught) {
            // Show the RPC error message to the user
          }

          public void onSuccess(String result) {
            presenter.go(RootPanel.get());
          }
        });
      }
    }

    // Add a handler to send the name to the server
    MyHandler handler = new MyHandler();
    logInWithAris.addClickHandler(handler);
    nameField.addKeyUpHandler(handler);
  }

  /**
   * Opens a new windows with a specified URL..
   *
   * @param url String with your URL.
   */
  public static void openNewWindow(String url) {
    String params = "menubar=no,location=false,resizable=yes,scrollbars=yes,status=no,dependent=true,width=1080,height=650";
    com.google.gwt.user.client.Window.open(url, "_blank", params);
    //After opening this window, we need to get the callback to the redirect uri
    //which is hosted by the redirect servlet
    //the servlet will have an HTTPClient to make further calls to the Auth Server
    //to get the Auth Code, and AccessToken + UserInfo
  }

  native void consoleLog( String message) /*-{
    console.log( "me:" + message );
  }-*/;
}
