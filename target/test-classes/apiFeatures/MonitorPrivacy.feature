Feature: Monitoring Privacy on by itself issue in all env's. Related tickets are: PLAT-1007,BLUU-5718,BLUU-5762

  @PrivacyMonitor
  Scenario Outline: Sign in via the UI and validate enable field values in cameraConfigList
    Given Go to Admin Portal "<url>" using username "<username>" and password "<password>"
    And Validate Admin Page HTTP status code is 200
    When Search for the user account using ian or username "<ian_username>" and capture Websocket Response
    And Validate Subscriber Page HTTP status code is 200
    And Validate WSS response is not empty and fields are extracted
    Then Validate ibpConfig array is not empty
    And Validate cameraConfiglist in ibpConfig Object is not empty
    And Validate cameraCameraStatusList in ibpConfig Object is not empty
    And Validate whether enabled field in cameraConfigList is true or false, if false log it in the report

    Examples:
      |url                                 |username                 |password   |ian_username|
      |https://admin.bluebyadt.com/#/login!|miralimirzayev@adt.com   |!look@book5|873093643    |
