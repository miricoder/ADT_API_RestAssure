package com.adt.ibp.backend_automation.stepDefinitions;

import com.adt.ibp.Utils.ConfigurationReader;
import com.adt.ibp.backend_automation.RequestBuilder.TokenBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import cucumber.api.Scenario;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class LoginWithJWT {
    String requestbodyToken;
    String requestbodySession;
    Scenario scenario;
    String response;
    Response respToken;
    Response respSession;
    ConfigurationReader conf;
    @Before
    public void keepScenario(Scenario scenario){ this.scenario=scenario;}

    @Given("Validate user create JWT Token with request body {string}")
    public void validateUserCreateJWTTokenWithRequestBody(String password) throws JsonProcessingException {
        //create request body for Account Validation and JWT Token Creation
        TokenBuilder tokenBuilder = new TokenBuilder();
        tokenBuilder.setPassword(password);

        //Convert reqBody object to string
        ObjectMapper obMap = new ObjectMapper();
        requestbodyToken=obMap.writerWithDefaultPrettyPrinter().writeValueAsString(tokenBuilder);
        //write response to report
        /**
         * To Do
         * Incase if you revert to junit 5.8.1 & cucumber 7.0.0 follow below java link which explains how to you can also create Scenario Interface
         * https://javadoc.io/static/io.cucumber/cucumber-core/4.7.2/index.html?io/cucumber/core/api/Scenario.html
         */
        scenario.write(requestbodyToken);
        scenario.embed(requestbodyToken.getBytes(),"application/json");


    }

    @When("When user submit post request and gets response")
    public void whenUserSubmitPostRequestAndGetsResponse() {
        RestAssured.baseURI = conf.getProperty("jwt-config.properties", "VALIDATE_ACCOUNT_CREATE_JWT_TOKEN");
                            respToken=given().
//                            auth().
//                            basic("alpha01","banana1!").
                            header("Authorization",
                        "Basic " + conf.getProperty("jwt-config.properties", "Auth_Basic"),
              "X_SESSION_TOKEN" + conf.getProperty("jwt-config.properties","X_SESSION_TOKEN")
                                    ).
                            contentType(ContentType.JSON).
//                            body(requestbodyToken).
                            when().
                            post();
                    response=respToken.prettyPrint();
                    //write account validator and jwt token created response to report
                    scenario.write(response);


    }

    @And("And user validates if response status code is {int}")
    public void andUserValidatesIfResponseStatusCodeIs(int statusCode) {assertEquals(statusCode, respToken.statusCode());}

    @Then("JWT Token used to  sign in as admin or sub with ian {int}")
    public void jwtTokenUsedToSignInAsAdminOrSub(int sessionPreferences) throws JsonProcessingException {
        //create request body for Account Validation and JWT Token Creation
//        LoginJWTsessionPreferences sessionBuilder = new LoginJWTsessionPreferences();
//        sessionBuilder.setSessionPreferences(780820052);

        //Convert reqBody object to string
        ObjectMapper obMap = new ObjectMapper();
//        requestbodySession=obMap.writerWithDefaultPrettyPrinter().writeValueAsString(sessionBuilder);
        //write response to report
        /**
         * To Do
         * Incase if you revert to junit 5.8.1 & cucumber 7.0.0 follow below java link which explains how to you can also create Scenario Interface
         * https://javadoc.io/static/io.cucumber/cucumber-core/4.7.2/index.html?io/cucumber/core/api/Scenario.html
         */
        scenario.write(requestbodySession);
        scenario.embed(requestbodySession.getBytes(),"application/json");
            RestAssured.baseURI = conf.getProperty("jwt-config.properties", "INT_LOGIN_WITH_JWT_SUB");
                            respSession = given().
                            headers(
                                    "Authorization",
                                    "Bearer " + conf.getProperty("jwt-config.properties", "TEMP_INT_JWT_TOKEN")).
                            contentType(ContentType.JSON).
                            body(requestbodySession).
                            when().
                            post();
                    response=respSession.prettyPrint();
                    //write sign session response to report
                    scenario.write(String.valueOf(respSession));
    }

    @And("Validate JWT token is received from response")
    public void validate_JWT_Token(String error) {
        String errorMessage=JsonPath.read(respSession, "$.error.validationErrors[*].message").toString();
        scenario.write("Error Message: "+errorMessage);
        assertTrue(errorMessage.contains(error));
    }
}
