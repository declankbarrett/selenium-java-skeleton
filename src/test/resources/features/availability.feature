@smoke @availability
Feature: Application availability
  As a tester
  I want to confirm the application is reachable
  So that I know the environment is ready for testing

  Scenario: Application is available
    Given I open the application
    Then the login page should be displayed
    And the page title should be "Swag Labs"
