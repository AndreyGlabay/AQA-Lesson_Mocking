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
            = new WireMockServer(new WireMockConfiguration().port(9098)); // (2.1.c) Add new WireMockServer;

    int delay = 3731;
    int code = 214;





    @BeforeClass // (2.1.d) Implements @BeforeClass annotation
    public void beforeClass(){
        wireMockServer.start(); // (2.1.e) Implements start of wireMockServer;
        WireMock.configureFor("localhost", port); // (2.1.f) Implements config creating using localhost & port #;
        WireMock.stubFor( // (2.2.a) Configure stubbing behavior for Request with ->
                WireMock.get(WireMock.urlEqualTo("/analytics")) // -> with Request HTTP-Method GET;
                        .willReturn(WireMock.aResponse() // (2.2.b) If req matched with URL - WireMock returns:
                                .withHeader("authorization", "Basic") // (2.3.a) Set the Header for resp.
                                .withStatus(code) //  status code = 200;
                                .withFixedDelay(delay)


                        )



        );
    }

    @AfterClass // (2.1.d) Implements @AfterClass annotation
    public void afterClass(){
        wireMockServer.stop(); // (2.1.g) Implements stop of wireMockServer;
    }

    @Test
    public void checkMyResponse() throws IOException { // (2.2.c) Implements method "checkMyUrl()" and ->
        var myUrl = "http://localhost:" + port + "/analytics"; // -> var = myURL (URL+port+Endpoint) ->
        var request = new Request.Builder() // -> and create new request;
                .url(myUrl)
                .build();
        try (var response = client.newCall(request).execute()) { // (2.2.d) Execute this new request;
            Assert.assertEquals(request.url().toString(), myUrl); // (2.2.e) Check actual URL = expected URL.
            Assert.assertEquals("Basic", response.header("authorization")); // (2.3.b) Check Header matching;
            // Assert.assertEquals(response.code(), 200);
        }
    }
}
