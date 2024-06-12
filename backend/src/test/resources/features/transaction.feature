Feature: Transaction

  Scenario: Creating a Transaction Successfully
    Given I am an authenticated user with role "EMPLOYEE"
    When I send a POST request to "/transactions/transfer" with valid transaction data
    Then the transaction response status should be 201
    And the response should contain the transaction ID