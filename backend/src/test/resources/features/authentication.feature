Feature: Authentication

  Scenario: Register with invalid email
    Given a user with an invalid email "invalid-email-format"
    When the user attempts to register
    Then the response status should be 400
    And the response message should be "Invalid email format"

  Scenario: Login with non-existent email
    Given a user with email "non.existent@example.com" and password "password"
    When the user attempts to login
    Then the response status should be 401
    And the response message should be "Invalid email"
