package com.aris.server;

import com.aris.client.IProcessMiningServlet;
import com.aris.shared.Verifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProcessMiningServletImpl extends RemoteServiceServlet implements
        IProcessMiningServlet {

  private HttpClient httpClient = HttpClientBuilder.create().build();

  public String redirectURI(String AuthCode) throws IllegalArgumentException {
    // Verify that the AuthCode is valid.
    if (!Verifier.isValid(AuthCode)) {
      // If the AuthCode is not valid, throw an IllegalArgumentException back to the client.
      throw new IllegalArgumentException(
          "Invalid Authorization Code has been provided...");
    }

    String accessToken = requestAccessToken(AuthCode);
    String userName = requestUserInfo(accessToken);
    return userName;
  }


  //HTTP Client will be provided here, which will make the following request
  private String requestAccessToken(String AuthCode) {

    return null;
  }

  private String requestUserInfo(String accessToken) {

    return null;
  }

  //This RPC Call will be made, once the redirect URI has been invoked
  private String returnFormattedUserInfo() {

    return null;
  }
}
