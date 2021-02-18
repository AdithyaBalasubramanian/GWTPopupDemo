package com.aris.admin.servlet;


import com.aris.admin.shared.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.thirdparty.guava.common.base.Charsets;
import com.google.gwt.thirdparty.guava.common.collect.ImmutableList;
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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Collection;

public class Server extends HttpServlet {
    private static final long serialVersionUID = 1508982861969399325L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doGet is invoked");
        String authCode = req.getParameter("code");
        String hostName = "10.248.91.205";
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

        //resp.getWriter().write(json.toJSONString());
        //writeToCookie(resp, json.toJSONString());

        //check why cookie is not working - same domain problem

        //write to file and read from file
        String path = "C:\\Users\\ADBA\\GWT-popup-Window\\src\\main\\java\\com\\aris\\admin\\shared\\person.properties";
        System.out.println("Trying to write the user details to file = " + path);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            //pretty print
            writer.write(json.toJSONString());
            writer.close();
            System.out.println("write completed successfully...");
        } catch (FileNotFoundException e) {
            System.out.println("error in writing to file");
            e.printStackTrace();
        }

        //move to another auto close html page and then render the content

    }

    private void writeToCookie(HttpServletResponse resp, String json) {
        Cookie cookie = new Cookie("person", json);
        cookie.setMaxAge(2147483647);
        cookie.setDomain("127.0.0.1");
        System.out.println("Cookie = " + cookie);
        resp.addCookie(cookie);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("doPost is invoked");
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
        String clientId = "2de8e473-6e51-438c-aafa-6ae3e0c94ea5";
        String clientSecret = "b13e7d08-9f29-4e22-89d8-9aa984ee4c3a";
        String redirectUrl = "http://localhost:8080/umc/redirect";
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
