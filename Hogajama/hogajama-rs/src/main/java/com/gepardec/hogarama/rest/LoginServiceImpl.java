package com.gepardec.hogarama.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gepardec.hogarama.domain.security.AccessTokenDTO;
import com.gepardec.hogarama.domain.security.LoginCredentialsDTO;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpHeaders.USER_AGENT;

@Path("/security")
public class LoginServiceImpl {

    @Context
    private ServletContext context;

    public static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);
    public static final ObjectMapper jsonMapper = new ObjectMapper();

    @POST
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(LoginCredentialsDTO loginCredentialsDTO) throws Exception {

        HttpClient client = getNewHttpClient();

        File file = new File(context.getRealPath("/WEB-INF/keycloak.json"));

        JsonNode keycloakNode = jsonMapper.readTree(new FileInputStream(file));
        String urlBase = keycloakNode.get("auth-server-url").asText();
        String clientSecret = keycloakNode.get("credentials").get("secret").asText();
        String realm = keycloakNode.get("realm").asText();

        HttpPost post = new HttpPost(urlBase + "realms/" + realm + "/protocol/openid-connect/token");

        // add header
        post.setHeader("User-Agent", USER_AGENT);

        List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
        urlParameters.add(new BasicNameValuePair("client_id", realm));
        urlParameters.add(new BasicNameValuePair("grant_type", "password"));
        urlParameters.add(new BasicNameValuePair("username", loginCredentialsDTO.getUsername()));
        urlParameters.add(new BasicNameValuePair("password", loginCredentialsDTO.getPassword()));
        urlParameters.add(new BasicNameValuePair("client_secret", clientSecret));

        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        HttpResponse response = client.execute(post);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }

        String json = result.toString();
        JsonNode jsonNode = jsonMapper.readTree(json);
        String accessTokenString = jsonNode.get("access_token").asText();

        AccessTokenDTO accessToken = new AccessTokenDTO();
        accessToken.setAccessToken(accessTokenString);
        accessToken.setValidityDuration(30);

        System.out.println(accessToken.getAccessToken());

        return Response.ok(accessToken).build();
    }

    public HttpClient getNewHttpClient() {

        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

}
