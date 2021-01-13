package com.aris.server;

import com.aris.client.IProcessMiningServlet;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jose4j.json.internal.json_simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static org.apache.commons.io.Charsets.UTF_8;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProcessMiningServletImpl extends RemoteServiceServlet implements
        IProcessMiningServlet {

  public String redirectURI(String AuthCode, String hostName) throws IllegalArgumentException, IOException {
    //Hostname will be available from the initial login web page call that this client makes

/*    if (!Verifier.isValid(AuthCode)) {
      // If the AuthCode is not valid, throw an IllegalArgumentException back to the client.
      throw new IllegalArgumentException(
          "Invalid Authorization Code has been provided...");
    }*/
    //Need to get the well known end point from the Auth Server...
    JSONObject wellKnownEndpoints = requestWellKnownEndpoints(hostName);
    Object token_endpoint = wellKnownEndpoints.get("token_endpoint");
    Object userinfo_endpoint = wellKnownEndpoints.get("userinfo_endpoint");
    String accessToken = requestAccessToken(AuthCode, String.valueOf(token_endpoint));
    return requestUserInfo(accessToken, String.valueOf(userinfo_endpoint));
  }

  private JSONObject requestWellKnownEndpoints(String hostname) throws IOException {
    HttpPost wellKnownEndpointsRequest = new HttpPost();
    HttpResponse response = firePostRequestAndParseResponse("", wellKnownEndpointsRequest, hostname);
    return extractJsonResponse(response);
  }

  private String requestAccessToken(String AuthCode, String hostName) throws IOException {
    HttpPost accessTokenRequest = new HttpPost();
    HttpResponse response =  firePostRequestAndParseResponse(AuthCode, accessTokenRequest, hostName);
    return extractStringResponse(response);
  }

  private String requestUserInfo(String accessToken, String hostName) throws IOException {
    HttpPost userInfoRequest = new HttpPost();
    HttpResponse response = firePostRequestAndParseResponse(accessToken, userInfoRequest, hostName);
    return extractStringResponse(response);
  }

  //This RPC Call will be made, once the redirect URI has been invoked
  private String returnFormattedUserInfo() {

    return null;
  }

  private HttpResponse firePostRequestAndParseResponse(String content, HttpPost request, String url) throws IOException {
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost host = new HttpHost(url);
    if (content != null && !content.isEmpty()) {
      request.setEntity(new StringEntity(content));
    }
    HttpResponse response = httpClient.execute(host, request);
    httpClient.close();
    return response;
  }

  private String extractStringResponse(HttpResponse response) throws IOException {
    InputStream inputStream = response.getEntity().getContent();
    return IOUtils.toString(inputStream, UTF_8);
  }

  private JSONObject extractJsonResponse(HttpResponse response) throws IOException {
    InputStream inputStream = response.getEntity().getContent();
    ObjectMapper mapper = new ObjectMapper();
    //Change String to the appropriate POJO for well known endpoint
    JSONObject json = mapper.readValue(inputStream, JSONObject.class);
    return json;
  }
}
