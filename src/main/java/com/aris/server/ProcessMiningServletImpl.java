package com.aris.server;

import com.aris.client.IProcessMiningServlet;
import com.aris.shared.Verifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.Charsets.UTF_8;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProcessMiningServletImpl extends RemoteServiceServlet implements
        IProcessMiningServlet {


  public String redirectURI(String AuthCode) throws IllegalArgumentException, IOException {
    if (!Verifier.isValid(AuthCode)) {
      // If the AuthCode is not valid, throw an IllegalArgumentException back to the client.
      throw new IllegalArgumentException(
          "Invalid Authorization Code has been provided...");
    }

    String accessToken = requestAccessToken(AuthCode);
    return requestUserInfo(accessToken);
  }


  //HTTP Client will be provided here, which will make the following request
  private String requestAccessToken(String AuthCode) throws IOException {
    HttpPost accessTokenRequest = new HttpPost();
    return fireRequestAndParseResponse(AuthCode, accessTokenRequest, "");
  }



  private String requestUserInfo(String accessToken) throws IOException {
    HttpPost userInfoRequest = new HttpPost();
    return fireRequestAndParseResponse(accessToken, userInfoRequest, "");
  }

  //This RPC Call will be made, once the redirect URI has been invoked
  private String returnFormattedUserInfo() {

    return null;
  }

  private static String fireRequestAndParseResponse(String authCode, HttpPost request, String hostName) throws IOException {
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost host = new HttpHost(hostName);
    request.setEntity(new StringEntity(authCode));
    HttpResponse response = httpClient.execute(host, request);
    InputStream inputStream = response.getEntity().getContent();
    String responseContent = IOUtils.toString(inputStream, UTF_8);
    httpClient.close();
    return responseContent;
  }
}
