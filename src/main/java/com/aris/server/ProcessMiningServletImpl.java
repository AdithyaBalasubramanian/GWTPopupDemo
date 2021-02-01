package com.aris.server;

import com.aris.client.IProcessMiningServlet;
import com.aris.shared.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.thirdparty.guava.common.base.Charsets;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.jose4j.json.internal.json_simple.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ProcessMiningServletImpl extends RemoteServiceServlet implements
        IProcessMiningServlet {

  public Person redirectURI(String authCode, String hostName) throws IllegalArgumentException, IOException {
    Person person;
    ObjectMapper mapper = new ObjectMapper();
    authCode = "1e0a0dde-3012-4fbc-a08f-cac66209e944";
    //Hostname will be available from the initial login web page call that this client makes

/*    if (!Verifier.isValid(AuthCode)) {
      // If the AuthCode is not valid, throw an IllegalArgumentException back to the client.
      throw new IllegalArgumentException(
          "Invalid Authorization Code has been provided...");
    }*/
    //Need to get the well known end point from the Auth Server...
    System.out.println("Reached redirectURI method...");
    JSONObject wellKnownEndpoints = requestWellKnownEndpoints(hostName);

    Object token_endpoint = wellKnownEndpoints.get("token_endpoint");
    System.out.println("Object Token endpoint = " + token_endpoint);
    String token = mapper.convertValue(token_endpoint, String.class);
    String [] values = token.split("localhost");
    String tokenEndpoint = values[1];
    System.out.println("String token endpoint = " + tokenEndpoint);

    //Extract method
    Object userinfo_endpoint = wellKnownEndpoints.get("userinfo_endpoint");
    System.out.println("Object User Info endpoint = " + userinfo_endpoint);
    String userInfo = mapper.convertValue(userinfo_endpoint, String.class);
    values = userInfo.split("localhost");
    String userInfoEndpoint = values[1];
    System.out.println("String userInfo endpoint = " + userInfoEndpoint);
    System.out.println("Request accessToken...");
    JSONObject response = requestAccessToken(authCode, hostName, tokenEndpoint);

    String accessToken = String.valueOf(response.get("accessToken"));
    System.out.println("Obtained accessToken = " + accessToken);
    System.out.println("Request UserInfo...");
    JSONObject json =  requestUserInfo(accessToken, hostName, userInfoEndpoint);
    person = mapper.convertValue(json, Person.class);
    System.out.println("Obtained userInfo = " + person);
    return person;
  }

  private JSONObject requestWellKnownEndpoints(String hostname) throws IOException {
    System.out.println("Reached wellKnownEndpoints call");
    HttpGet wellKnownEndpointsRequest = new HttpGet("/umc/api/v1/oauth/knownurl?tenant=test01");
    return fireRequestAndParseResponse(wellKnownEndpointsRequest, hostname);
  }

  private JSONObject requestAccessToken(String AuthCode, String hostName, String endpoint) throws IOException, UnknownHostException {
    System.out.println("Reached requestAccessToken call");
    HttpPost accessTokenRequest = new HttpPost(endpoint);
    String clientId = "f8fada5e-06ed-4f05-9282-c42e49244751";
    String clientSecret = "79aafe4c-3fbe-4983-bbb5-4ca3a548f56d";
    String redirectUrl = "http://localhost:8080/umc/redirect";
    String scope = "Import";
    final Collection<NameValuePair> formParams = ImmutableList.of(
          new BasicNameValuePair("client_id", clientId),
          new BasicNameValuePair("client_secret", clientSecret),
          new BasicNameValuePair("code", AuthCode),
          new BasicNameValuePair("redirect_uri", redirectUrl),
          new BasicNameValuePair("oauth_scope", scope)
    );
    JSONObject json =  fireRequestAndParseResponse(new UrlEncodedFormEntity(formParams, Charsets.UTF_8), accessTokenRequest, hostName);
    return json;
  }

  private JSONObject requestUserInfo(String accessToken, String hostName, String endpoint) throws IOException {
    System.out.println("Reached requestUserInfo call");
    HttpPost userInfoRequest = new HttpPost(endpoint + "?oauth_token="+accessToken);
    JSONObject json = fireRequestAndParseResponse(null, userInfoRequest, hostName);
    return json;
  }

  private JSONObject fireRequestAndParseResponse(HttpGet request, String url) throws IOException, UnknownHostException {
    System.out.println("Fire GET request and Parse called with URL = " + url);
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost target = new HttpHost(url, 80, "http");

    BasicHttpContext localContext = new BasicHttpContext();
    System.out.println("Constructed host = " + target.toURI() + request.getURI());
    HttpResponse response = httpClient.execute(target, request, localContext);
    JSONObject jsonObject = extractJsonResponse(response);
    httpClient.close();
    return jsonObject;
  }

  private JSONObject fireRequestAndParseResponse(HttpEntity entity, HttpPost request, String url) throws IOException, UnknownHostException {
    System.out.println("Fire POST request and Parse called with URL = " + url);
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost host = new HttpHost(url, 80, "http");
    BasicHttpContext localContext = new BasicHttpContext();

    if (entity != null) {
      request.setEntity(entity);
    }
    System.out.println("Constructed host = " + host.toURI() + request.getURI());
    HttpResponse response = httpClient.execute(host, request, localContext);
    JSONObject jsonObject = extractJsonResponse(response);
    httpClient.close();
    return jsonObject;
  }

  private JSONObject extractJsonResponse(HttpResponse response) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    System.out.println("Reached extractJsonResponse call");
    System.out.println("Status response = " + response.getStatusLine().getStatusCode());
    InputStream inputStream = response.getEntity().getContent();
    //Change String to the appropriate POJO for well known endpoint
    JSONObject json = mapper.readValue(inputStream, JSONObject.class);
    System.out.println("Got JSON Response = " + json);
    inputStream.close();
    return json;
  }

  private String extractStringResponse(HttpResponse response) throws IOException {
    System.out.println("Reached extractStringResponse call");
    System.out.println("Status response = " + response.getStatusLine().getStatusCode());
    return EntityUtils.toString(response.getEntity());
  }
}
