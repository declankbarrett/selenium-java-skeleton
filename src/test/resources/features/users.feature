@users
Feature: Users
  As an application user
  I want to view and create users
  So that user records can be managed

  @smoke
  Scenario: Seeded users are displayed
    Given I open the users page
    Then the users list should contain seeded users

  @smoke
  Scenario: View a user's details
    Given I open the users page
    When I view the seeded developer
    Then the developer details should include their email

  @functional
  Scenario: Create a user
    Given I open the users page
    When I create a new user
    Then the new user should appear in the users list

  @functional
  Scenario: Empty user form shows validation
    Given I open the new user form
    When I submit the empty user form
    Then the user form validation error should be displayed
