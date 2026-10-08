@logout
Feature: Logout
  As a logged in user
  I want to log out of the application
  So that my session is closed

  @smoke
  Scenario: Successful Logout
    Given I am logged into the application
    When I log out
    Then I should be returned to the login page
