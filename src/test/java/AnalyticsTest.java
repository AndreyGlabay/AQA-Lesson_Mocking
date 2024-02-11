import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class AnalyticsTest { // (2.1.a) In the package "com.example.school.tests" create new test class "AnalyticsTest";
    int port = 9098; // (2.1.f) Implements var "port" and initiate it;
    OkHttpClient client = new OkHttpClient.Builder().build(); // (2.1.b) Implements new OkHttpClient;
    WireMockServer wireMockServer
            = new WireMockServer(new WireMockConfiguration().port(9098));//(2.1.c)Implements new WireMockServer;

    @BeforeClass // (2.1.d) Implements @BeforeClass annotation
    public void beforeClass(){
        wireMockServer.start(); // (2.1.e) Implements start of wireMockServer;
        WireMock.configureFor(port); // (2.1.f) Implements configuration creating using port num;
        WireMock.stubFor( // (2.2.a) Configure stubbing behavior: ->
                WireMock.get( // -> set up behavior for Request HTTP-Method GET ->
                        WireMock.urlEqualTo("/analytics") // -> and set up behavior for Request URL ->
                ).willReturn(WireMock.aResponse().withStatus(200)) // (2.2.b) If Request matched with listed ->
                // -> in the (step 2.2.a) - WireMock returns this object with status code = 200;
        );
    }

    @AfterClass // (2.1.d) Implements @AfterClass annotation
    public void afterClass(){
        wireMockServer.stop(); // (2.1.g) Implements stop of wireMockServer;
    }

    @Test
    public void checkMyUrl() throws IOException { // (2.2.c) Implements method "checkMyUrl()" and ->
        var myUrl = "http://localhost:" + port + "/analytics"; // -> var = myURL (URL+port+Endpoint) ->
        var request = new Request.Builder() // -> and create new request;
                .url(myUrl)
                .build();
        try (var response = client.newCall(request).execute()) { // (2.2.d) Execute this new request;
            Assert.assertEquals(request.url().toString(), myUrl); // (2.2.e) Check actual URL = expected URL.
            // Assert.assertEquals(response.code(), 200);
        }
    }
}
