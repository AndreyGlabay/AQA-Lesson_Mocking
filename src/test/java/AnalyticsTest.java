//package java.test; // (2.1.a) In the dir src/test/java create new package "com.example.school.tests";

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

public class AnalyticsTest { // (2.1.b) In the package "com.example.school.tests" create new test class "AnalyticsTest";
    int port = 9098; // (2.1.g) Implements var "port" and initiate it;
    OkHttpClient client = new OkHttpClient.Builder().build(); // (2.1.c) Implements new OkHttpClient;
    WireMockServer wireMockServer
            = new WireMockServer(new WireMockConfiguration().port(9098));//(2.1.d)Implements new WireMockServer;

    @BeforeClass // (2.1.e) Implements @BeforeClass annotation
    public void beforeClass(){
        wireMockServer.start(); // (2.1.f) Implements start of wireMockServer;
        WireMock.configureFor(port); // (2.1.g) Implements configuration creating using port num;
        WireMock.stubFor(
                WireMock.get(
                        WireMock.urlEqualTo("/analytics")
                ).willReturn(WireMock.aResponse().withStatus(200))
        );
    }

    @AfterClass // (2.1.e) Implements @AfterClass annotation
    public void afterClass(){
        wireMockServer.stop(); // (2.1.f) Implements stop of wireMockServer;
    }

    @Test
    public void checkMyUrl() throws IOException {
        var request = new Request.Builder()
                .url("http://localhost:" + port + "/analytics")
                .build();
        try (var response = client.newCall(request).execute()) {
            Assert.assertEquals(response.code(), 200);
        }
    }
}
