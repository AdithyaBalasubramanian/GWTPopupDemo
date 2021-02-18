package com.aris.admin.server;


import com.aris.admin.client.IProcessMiningServlet;
import com.aris.admin.shared.Person;
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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.jose4j.json.internal.json_simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

  }

  @Override
  public Person readPersonFromFile(String fileName) throws IllegalArgumentException, IOException {
    return new Person("adba", "adba", "adba");
  }

  public Person redirectURI(String authCode, String hostName) throws IllegalArgumentException, IOException {
    Person person;
    ObjectMapper mapper = new ObjectMapper();
    //Hostname will be available from the initial login web page call that this client makes

/*    if (!Verifier.isValid(AuthCode)) {
      // If the AuthCode is not valid, throw an IllegalArgumentException back to the client.
      throw new IllegalArgumentException(
          "Invalid Authorization Code has been provided...");
    }*/
    //Need to get the well known end point from the Auth Server...

    System.out.println("Reached redirectURI method...");
    JSONObject wellKnownEndpoints = requestWellKnownEndpoints(hostName);

   String tokenEndpoint = getEndPoint(hostName, mapper, wellKnownEndpoints, "token_endpoint");

    //Extract method
    String userInfoEndpoint = getEndPoint(hostName, mapper, wellKnownEndpoints, "userinfo_endpoint");

    System.out.println("Request accessToken...");
    String accessToken = requestAccessToken(authCode, hostName, tokenEndpoint);
    System.out.println("Obtained accessToken = " + accessToken);

    System.out.println("Request UserInfo...");
    JSONObject jsonObject =  requestUserInfo(accessToken, hostName, userInfoEndpoint);
    JSONObject json = mapper.convertValue(jsonObject.get(".APerson"), JSONObject.class);
    person = new Person();
    person.setFirstName(String.valueOf(json.get("firstname")));
    person.setLastName(String.valueOf(json.get("lastname")));
    person.setUserId(String.valueOf(json.get("name")));
    System.out.println("Obtained userInfo = " + person);
    return person;
  }

  private String getEndPoint(String hostName, ObjectMapper mapper, JSONObject wellKnownEndpoints, String endpoint) {
    String[] values;
    Object endPoint = wellKnownEndpoints.get(endpoint);
    System.out.println("Object value = " + endPoint);
    String userInfo = mapper.convertValue(endPoint, String.class);
    values = userInfo.split(hostName +":14480");
    String result = values[1];
    System.out.println("String value = " + result);
    return result;
  }

  private JSONObject requestWellKnownEndpoints(String hostname) throws IOException {
    System.out.println("Reached wellKnownEndpoints call");
    HttpGet wellKnownEndpointsRequest = new HttpGet("/umc/api/v1/oauth/knownurl?tenant=test01");
    return fireRequestAndParseResponse(wellKnownEndpointsRequest, hostname);
  }

  private String requestAccessToken(String AuthCode, String hostName, String endpoint) throws IOException, UnknownHostException {
    System.out.println("Reached requestAccessToken call");
    HttpPost accessTokenRequest = new HttpPost(endpoint);
    String clientId = "14490c74-e1b8-4f7b-a1b4-8ebc9ab05157";
    String clientSecret = "0c14e892-ff5b-48fb-a72b-7338636c7e8d";
    String redirectUrl = "http://localhost:8080/login/umc/redirect";
    String scope = "UserProfile";
    final Collection<NameValuePair> formParams = ImmutableList.of(
          new BasicNameValuePair("client_id", clientId),
          new BasicNameValuePair("client_secret", clientSecret),
          new BasicNameValuePair("code", AuthCode),
          new BasicNameValuePair("redirect_uri", redirectUrl),
          new BasicNameValuePair("oauth_scope", scope),
          new BasicNameValuePair("grant_type", "authorization_code"),
          new BasicNameValuePair("tenant", "default")

    );
    String token =  fireRequestAndParseResponse(new UrlEncodedFormEntity(formParams, Charsets.UTF_8), accessTokenRequest, hostName);
    return token;
  }

  private JSONObject requestUserInfo(String accessToken, String hostName, String endpoint) throws IOException {
    System.out.println("Reached requestUserInfo call");
    HttpPost userInfoRequest = new HttpPost(endpoint );
    userInfoRequest.addHeader("Authorization", "Bearer "+accessToken);
    JSONObject json = fireRequestAndParseResponse( userInfoRequest, hostName);
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

  private String fireRequestAndParseResponse(HttpEntity entity, HttpPost request, String url) throws IOException, UnknownHostException {
    System.out.println("Fire POST request and Parse called with URL = " + url);
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost host = new HttpHost(url, 80, "http");
    BasicHttpContext localContext = new BasicHttpContext();

    if (entity != null) {
      request.setEntity(entity);
    }

    System.out.println("Constructed host = " + host.toURI() + request.getURI());
    HttpResponse response = httpClient.execute(host, request, localContext);
    String stringResponse = extractStringResponse(response);
    httpClient.close();
    return stringResponse;
  }

  private JSONObject fireRequestAndParseResponse(HttpPost request, String url) throws IOException, UnknownHostException {
    System.out.println("Fire POST request (no entity attached) and Parse called with URL = " + url);
    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
    HttpHost host = new HttpHost(url, 80, "http");
    BasicHttpContext localContext = new BasicHttpContext();

    System.out.println("Constructed host = " + host.toURI() + request.getURI());
    HttpResponse response = httpClient.execute(host, request, localContext);
    JSONObject json = extractJsonResponse(response);
    httpClient.close();
    return json;
  }

  private JSONObject extractJsonResponse(HttpResponse response) throws IOException {
    System.out.println("Reached extractJsonResponse call");
    ObjectMapper mapper = new ObjectMapper();
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
