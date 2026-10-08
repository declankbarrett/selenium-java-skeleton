@smoke @availability
Feature: Application availability
  As a tester
  I want to confirm the application is reachable
  So that I know the environment is ready for testing

  Scenario: Users page is available
    Given I open the users page
    Then the users page should be displayed
    And the page title should be "Test Application"

  Scenario: Projects page is available
    Given I open the projects page
    Then the projects page should be displayed
    And the page title should be "Test Application"
