@projects
Feature: Projects
  As an application user
  I want to view and create projects
  So that project records can be managed

  @functional
  Scenario: Create a project and confirm its members
    Given I open the new project form
    When I create a new project
    Then the project member selection page should show the new project
    When I confirm the project member selection
    Then the new project should appear in the projects list

  @functional
  Scenario: Empty project form shows validation
    Given I open the new project form
    When I submit the empty project form
    Then the project form validation error should be displayed
