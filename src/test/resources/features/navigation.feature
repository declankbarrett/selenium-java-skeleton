@navigation
Feature: Navigation
  As a user
  I want to move between the users and projects sections
  So that I can access both parts of the application

  @functional
  Scenario: Navigate from users to projects and back
    Given I open the users page
    When I navigate to the projects page
    Then the projects page should be displayed
    When I navigate back to the users page
    Then the users page should be displayed
