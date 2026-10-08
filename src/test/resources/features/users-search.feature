# Work item: adhoc-20261008-users-search-filter (test plan: qa-work/adhoc-20261008-users-search-filter/outputs/test-plan.md)
# Runs against the Test Application (appBaseUrl/apiUrl). Seed users are asserted by email; totals are never asserted
# because the shared database may contain rows from other runs.
# Search/filter element IDs are the proposed contract from open question Q9 (see pages.UsersListPage).
@search
Feature: Search and filter users on the Users list
  As a project manager
  I want to search and filter the users list
  So that I can find the right people quickly

  @functional @S01
  Scenario Outline: Typing a search narrows the list by partial, case-insensitive text
    Given I am on the users list
    When I search the users list for "<search>"
    Then every listed user should match the search "<search>"
    And the users list should include "<included>"
    And the users list should not include "<excluded>"
    And the listed users should match the API results for search "<search>"

    Examples:
      | search    | included           | excluded           |
      | anna      | developer@test.com | manager@test.com   |
      | KOWAL     | tester@test.com    | developer@test.com |
      | business@ | business@test.com  | developer@test.com |

  @functional @S02
  Scenario Outline: Searching by full name finds the user
    Given I am on the users list
    When I search the users list for "<search>"
    Then every listed user should match the search "<search>"
    And the users list should include "<included>"
    And the users list should not include "<excluded>"

    Examples:
      | search       | included           | excluded           |
      | anna nowak   | developer@test.com | business@test.com  |
      | jan kowalski | manager@test.com   | developer@test.com |
      | jan kowalski | tester@test.com    | developer@test.com |

  @functional @S03
  Scenario: The position filter lists each existing position once, alphabetically
    Given I am on the users list
    Then the position filter should list each existing position once in alphabetical order
    And the position filter should show "All positions"

  @functional @S03
  Scenario: Filtering by position shows only users with that position
    Given I am on the users list
    When I filter the users list by position "Test Engineer"
    Then every listed user should have position "Test Engineer"
    And the users list should include "tester@test.com"
    And the users list should not include "manager@test.com"

  @functional @S04
  Scenario: Search and position filter combine
    Given I am on the users list
    When I search the users list for "kowalski"
    And I filter the users list by position "Test Engineer"
    Then every listed user should match the search "kowalski" and have position "Test Engineer"
    And the users list should include "tester@test.com"
    And the users list should not include "manager@test.com"

  @functional @S04
  Scenario: Clear resets the search and the position filter
    Given I have searched the users list for "kowalski" and filtered by position "Test Engineer"
    When I clear the users search
    Then the users search box should be empty
    And the position filter should show "All positions"
    And the users list should include all seeded users

  @functional @S05
  Scenario: A search with no matches shows a message instead of an empty table
    Given I am on the users list
    When I search the users list for "zz-no-such-user"
    Then the no users match message should be displayed
    And no user rows should be listed

  @functional @data-write @S06
  Scenario: View details works on a filtered result
    Given two users sharing a unique search token exist with position "Junior Developer"
    And I have filtered the users list to the shared token and position "Junior Developer"
    When I view the first token user from the filtered list
    Then the user details should show the first token user's email

  @functional @data-write @S06
  Scenario: Update user works on a filtered result
    Given two users sharing a unique search token exist with position "Junior Developer"
    And I have filtered the users list to the shared token and position "Junior Developer"
    When I open the update form for the first token user from the filtered list
    Then the update form should be pre-filled with the first token user's email

  @functional @data-write @S06
  Scenario: Removing a user keeps the current search and filter applied
    Given two users sharing a unique search token exist with position "Junior Developer"
    And I have filtered the users list to the shared token and position "Junior Developer"
    When I remove the first token user from the filtered list
    Then only the second token user should be listed
    And the users search box should still contain the shared token
    And the position filter should show "Junior Developer"

  @functional @security @S08
  Scenario: A wildcard character in the search box is matched as plain text
    Given I am on the users list
    When I search the users list for "%"
    Then the no users match message should be displayed
    And no user rows should be listed
