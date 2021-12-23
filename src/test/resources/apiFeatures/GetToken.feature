Feature: Jwt Token API

  @LoginWithJWT @APIRegression @APISmoke
  Scenario Outline: Post Call - Positive scenario
    Given Validate user create JWT Token with request body "<password>"
    When When user submit post request and gets response
    And And user validates if response status code is 200
    Then JWT Token used to  sign in as admin or sub with ian <sessionPreferences>
    And Validate JWT token is received from response

    Examples:
      |password|sessionPreferences|
      |banana1!|780820052         |



