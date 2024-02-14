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
import java.time.Duration;
import java.time.Instant;

public class AnalyticsTest { // (2.1.a) In the package "com.example.school.tests" create new test class "AnalyticsTest";
    int port = 9098; // (2.1.f) Implements var "port" and initiate it;
    OkHttpClient client = new OkHttpClient.Builder().build(); // (2.1.b) Implements new OkHttpClient;
    WireMockServer wireMockServer
            = new WireMockServer(new WireMockConfiguration().port(9098)); // (2.1.c) Add new WireMockServer;
    int code = 214;     // (2.4.a) Implements var for httpResponseCode;
    int delay = 3731;   // (2.4.a) Implements var for httpResponseDelay;

    @BeforeClass // (2.1.d) Implements @BeforeClass annotation
    public void beforeClass(){
        wireMockServer.start(); // (2.1.e) Implements start of wireMockServer;
        WireMock.configureFor("localhost", port); // (2.1.f) Implements config creating using localhost & port #;
        WireMock.stubFor( // (2.2.a) Configure stubbing behavior for Request with ->
                WireMock.get(WireMock.urlEqualTo("/analytics")) // -> with Request HTTP-Method GET;
                        .willReturn(WireMock.aResponse() // (2.2.b) If req matched with URL - WireMock returns:
                                .withHeader("authorization", "Basic") //(2.3.a) Set the Header for response;
                                .withStatus(code) // (2.4.b) Set the Status Code for response;
                                .withFixedDelay(delay) // (2.4.c) Set the Fixed Delay for response;
                        )
        );
    }

    @AfterClass // (2.1.d) Implements @AfterClass annotation
    public void afterClass(){
        wireMockServer.stop(); // (2.1.g) Implements stop of wireMockServer;
    }

    @Test (testName = "TestCase1: Check Request Code + URL + Header")
    public void checkMyResponse() throws IOException {          // (2.2.c) Implements method "checkMyUrl()" and ->
        var myUrl = "http://localhost:" + port + "/analytics";  //          -> var = myURL (URL+port+Endpoint) ->
        var request = new Request.Builder()                     //          -> and create new request;
                .url(myUrl)
                .build();

        try (var response = client.newCall(request).execute()) { // (2.2.d) Execute this new request;
            Assert.assertEquals(response.code(), code); // (2.5.a) Check Status Code matching;
            Assert.assertEquals(request.url().toString(), myUrl); // (2.2.e) Check actual URL = expected URL.
            Assert.assertEquals("Basic", response.header("authorization")); // (2.3.b) Check Header matching;
        }
    }

    @Test (testName = "TestCase2: Check Request's Delay")
    public void checkMyResponseDelay() throws IOException {     // (4.1) Implements this annotation to divide one @Test ->
        var myUrl = "http://localhost:" + port + "/analytics";  //       -> by two. In this one remain check for
        var request = new Request.Builder()                     //          -> Fixed Delay.
                .url(myUrl)
                .build();
       Instant beforeRequest = Instant.now(); // (2.5.b) Measure time moment before Request execution;
        try (var response = client.newCall(request).execute()) { // (2.2.d) Execute this new request;
            Instant afterRequest = Instant.now(); // (2.5.c) Measure time moment after Request execution;
            int actualDelay = (int) Duration.between(beforeRequest, afterRequest).toMillis(); // (2.5.d) Get difference;
            Assert.assertTrue(actualDelay >= delay); // (2.5.e) Check Delay is equal or greater than expected;
        }
    }
}
// (step 3.2) This comment is tech changes in the code for get a difference for PR creation.