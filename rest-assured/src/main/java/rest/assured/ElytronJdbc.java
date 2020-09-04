package rest.assured;

import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.cookie.CookieFilter;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.params.CoreConnectionPNames;

import static io.restassured.config.HttpClientConfig.httpClientConfig;
import static org.hamcrest.Matchers.containsString;

public class ElytronJdbc
{
    static final String BASE_URI = String.format(
        "http://%s:8081"
        , System.getProperty("host")
    );

    public static void main(String[] args)
    {
        System.out.printf("Base URI is %s", BASE_URI);

        doNotFail(ElytronJdbc::anonymous);
//        doNotFail(ElytronJdbc::authenticated);
        doNotFail(ElytronJdbc::authenticated_not_authenticated);
        doNotFail(ElytronJdbc::forbidden);
        doNotFail(ElytronJdbc::forbidden_not_authenticated);
    }

    static void doNotFail(Runnable r)
    {
        try
        {
            r.run();
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
    }

    static void anonymous()
    {
        RestAssured.given()
            .when()
            .baseUri(BASE_URI)
            .get("/api/anonymous")
            .then()
            .statusCode(200)
            .body(containsString("anonymous"));
    }

    static void authenticated()
    {
        CookieFilter cookies = new CookieFilter();
        RestAssured.given()
            .redirects().follow(false)
            .filter(cookies)
            .when()
            .baseUri(BASE_URI)
            .get("/api/authenticated")
            .then()
            .statusCode(302);

        RestAssured
            .given()
            .filter(cookies)
            .redirects().follow(false)
            .when()
            .baseUri(BASE_URI)
            .formParam("j_username", "user")
            .formParam("j_password", "user")
            .post("/j_security_check")
            .then()
            .assertThat()
            .statusCode(302);

        RestAssured.given()
            .redirects().follow(false)
            .filter(cookies)
            .when()
            .baseUri(BASE_URI)
            .get("/api/authenticated")
            .then()
            .statusCode(200)
            .body(containsString("authenticated"));
    }

    static void authenticated_not_authenticated()
    {
        RestAssured.given()
            .redirects().follow(false)
            .when()
            .baseUri(BASE_URI)
            .get("/api/authenticated")
            .then()
            .statusCode(302);
    }

    static void forbidden()
    {
        CookieFilter cookies = new CookieFilter();
        RestAssured
            .given()
            // .config(shortTimeoutConfig())
            .filter(cookies)
            .redirects().follow(false)
            .when()
            .baseUri(BASE_URI)
            .formParam("j_username", "user")
            .formParam("j_password", "user")
            .post("/j_security_check")
            .then()
            .assertThat()
            .statusCode(302);

        RestAssured.given()
            .filter(cookies)
            .when()
            .baseUri(BASE_URI)
            .get("/api/forbidden")
            .then()
            .statusCode(403);
    }

    static void forbidden_not_authenticated()
    {
        RestAssured.given()
            .redirects().follow(false)
            .when()
            .baseUri(BASE_URI)
            .get("/api/forbidden")
            .then()
            .statusCode(302);
    }

    static RestAssuredConfig shortTimeoutConfig()
    {
        return RestAssured.config().httpClient(
            httpClientConfig()
                .setParam(ClientPNames.CONN_MANAGER_TIMEOUT, 30_000L)
                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 30_000)
                .setParam(CoreConnectionPNames.SO_TIMEOUT, 30_000)
        );


//            .custom()
//            .setConnectTimeout(5000)
//            .setConnectionRequestTimeout(5000)
//            .setSocketTimeout(5000)
//            .build();
//
//        HttpClientConfig httpClientFactory = HttpClientConfig.httpClientConfig()
//            .httpClientFactory(
//                () -> HttpClientBuilder
//                    .create()
//                    .setDefaultRequestConfig(requestConfig)
//                    .build()
//            );
//
//        return RestAssured
//            .config()
//            .httpClient(httpClientFactory);
    }

}
