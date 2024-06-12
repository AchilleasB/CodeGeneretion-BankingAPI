Feature: Account management

  Scenario: Get all accounts as an authenticated EMPLOYEE
    Given I am an authenticated EMPLOYEE
    When I request to get all accounts
    Then I should receive a list of all accounts
    And the response status should be 200 OK

