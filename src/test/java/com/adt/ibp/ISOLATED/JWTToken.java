package com.adt.ibp.ISOLATED;

import com.adt.ibp.backend_automation.RequestBuilder.SessionPrefOBJ;
import com.adt.ibp.backend_automation.RequestBuilder.SessionPrefSUBSCRIBER;
import com.adt.ibp.backend_automation.RequestBuilder.TokenBuilder;
import com.adt.ibp.Utils.ConfigurationReader;
import com.adt.ibp.Utils.CreateFileWriteToFile;
import com.adt.ibp.Utils.FileReader;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

/***
 * Resource to use to understandt ACS, SSO and OAUTH
 * https://adt-product.atlassian.net/wiki/spaces/ACS/pages/62914615/ACS+Auth
 */



public class JWTToken {
    SessionFilter sessionFilter = new SessionFilter();
    ConfigurationReader conf;
    CreateFileWriteToFile dynoValues;
    Response respToken;
    TokenBuilder tokenBuilder = new TokenBuilder();
    String jwtToken;
    public String sessionToken;
//    private final String CONTEXT_PATH_LOGIN = "rest/v12/subscriber/login"; /myingridStruts/rest/v13/subscriber/login;
    private final String CONTEXT_PATH_SSO = "v2/sso/BLUE_ALP/users/alpha01";
    private final String CONTEXT_PATH_LOGINwJWT_SUB="/rest/v14/subscriber/login";
    private final String CONTEXT_PATH_ACSAUTH="/rest/v14//admin/acsauth";
    private final String CONTEXT_PATH_ADMIN_USER="/rest/v14/csr/adminuser";

    /***
     * createdBy: @miralimirzayev
     * Got it working on Dec 5 11:55AM (SUNDAY)
     * Add X-Session token to TRY1 & TRY2
     */
    @BeforeAll
    @Test
    public void validateAccountCreateJWTToken(){
        /*
        GIVEN: Create Request Body for user validation with password "<password>"
        WHEN: Submit Post request and validate response is 200
        THEN: Validate and extract accessToken for subsequent sign in attempts
         */
        TokenBuilder reqCreateJWTtokenBody = new TokenBuilder();
        reqCreateJWTtokenBody.setPassword("banana1!");
        RestAssured.baseURI = conf.getProperty("jwt-config.properties", "URI_CreateJWTToken_Integration");
        Response response = given().
                relaxedHTTPSValidation().
                filter(sessionFilter).
                header("Authorization", "Basic "+conf.getProperty("jwt-config.properties", "Basic")).
                header("User-Agent", "iOS|9.1").
                contentType("application/json").
                config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))).
                body(reqCreateJWTtokenBody).
                when().log().all().
                post(CONTEXT_PATH_SSO).
                then().log().all().
                assertThat().
                statusCode(200).
                contentType("application/json").extract().response();
        String respToString = response.asString();
        JsonPath rowToJson = new JsonPath(respToString);
        /**
         * Basic Inforation Extraction from JSON Response Body
         */
        String identity = rowToJson.get("identity");
        String username = rowToJson.get("username");
        String realm = rowToJson.get("realm");
        String emailAddress = rowToJson.get("emailAddress");

        System.out.println("[identity: " + identity + "]");
        System.out.println("[username: " + username + "]");
        System.out.println("[realm: " + realm + "]");
        System.out.println("[emailAddress: " + emailAddress + "]");
        /**
         * Extract Access Token
         */
        System.out.println("Extracting accessToken for Subsequent Login Request");
        String accessToken = rowToJson.get("accessToken");
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-**-*-*-*");
        System.out.println("");
        System.out.println("Pre Extract:::> [accessToken: " + accessToken + "]");
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-BELOW IS THE CLEANED accessToken*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*");
        System.out.println("");
        jwtToken=accessToken.replace("accessToken: ", "");
        System.out.println(jwtToken);
        System.out.println("*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-**-*-*-*-*-*-*-**-*-*-*");
        System.out.println("");
//        DynamicValues dynoValues = new DynamicValues("src/test/resources/DynamicValueFiles/", "jwtToken.txt","Blueh");
        CreateFileWriteToFile dynoValues = new CreateFileWriteToFile("src/test/resources/DynamicValueFiles/","jwtToken.txt",jwtToken);
    }

    @Test
public void loginWithJWT_SUB(){
        SessionPrefOBJ sessionObj = new SessionPrefOBJ();
        SessionPrefSUBSCRIBER sessionPreIAN  = new SessionPrefSUBSCRIBER();
        sessionPreIAN.setIan(780820052);
        sessionObj.setSessionPreferences(sessionPreIAN);
        FileReader jwtRead = new FileReader("src/test/resources/DynamicValueFiles/jwtToken.txt");
        String token = jwtRead.readFile();

        RestAssured.baseURI = conf.getProperty("jwt-config.properties", "URI_LOGINwJWT_Integration");

    Response response = given().
            relaxedHTTPSValidation().
            filter(sessionFilter).
//            header("Authorization", "Basic "+conf.getProperty("jwt-config.properties", "Basic")).
            header("Authorization","Basic bWlyYWxpbWlyemF5ZXZAYWR0LmNvbTohbG9va0Bib29rNQ=").
            header("Authorization", "Bearer "+token).
            header("User-Agent", "iOS|9.1").
//            header("Cookie",conf.getProperty("jwt-config.properties","BLUE_SECURE")).
            contentType("application/json").
            config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false))).
            body(sessionObj).
            when().log().all().
            post(CONTEXT_PATH_LOGINwJWT_SUB).
            then().log().all().
            assertThat().
            statusCode(200).
            contentType("application/json").extract().response();
    String respToString = response.asString();
    JsonPath rowToJson = new JsonPath(respToString);

}
}
