@navigation
Feature: Navigation
  As a logged in user
  I want to move around the application
  So that I can reach the dashboard from anywhere

  @smoke
  Scenario: Navigate to dashboard
    Given I am logged into the application
    When I navigate to the dashboard
    Then the dashboard should be displayed

  @regression
  Scenario: Return to the dashboard from the shopping cart
    Given I am logged into the application
    And I am viewing my shopping cart
    When I navigate to the dashboard
    Then the dashboard should be displayed
