# Work item: adhoc-20261008-users-search-filter (test plan: qa-work/adhoc-20261008-users-search-filter/outputs/test-plan.md)
# API checks for GET /users?search=&position= and GET /users/positions (apiUrl). The browser hook still starts a
# driver for these scenarios because the project's Hooks are global.
@search @api
Feature: Search and filter users through the users API
  As a client of the users API
  I want optional search and position parameters on GET /users
  So that the Users list can be narrowed on the server

  @functional @S07
  Scenario: GET /users without parameters still returns every user
    When I request the users API without parameters
    Then the API response status should be 200
    And the API response should include all seeded users

  @functional @S07
  Scenario Outline: GET /users applies the optional search and position parameters
    When I request the users API with search "<search>" and position "<position>"
    Then the API response status should be 200
    And every user in the API response should match search "<search>" and position "<position>"
    And the API response should include "<included>"
    And the API response should not include "<excluded>"

    Examples:
      | search       | position        | included           | excluded           |
      | ANNA         |                 | developer@test.com | manager@test.com   |
      | jan kowalski |                 | manager@test.com   | developer@test.com |
      | jan kowalski |                 | tester@test.com    | business@test.com  |
      |              | Test Engineer   | tester@test.com    | manager@test.com   |
      | kowalski     | Project Manager | manager@test.com   | tester@test.com    |

  @functional @S05 @S07
  Scenario: GET /users returns an empty list when nothing matches
    When I request the users API with search "zz-no-such-user" and position ""
    Then the API response status should be 200
    And the API response should be an empty list

  @functional @S03 @S07
  Scenario: GET /users/positions returns each existing position once
    When I request the user positions API
    Then the API response status should be 200
    And the positions response should list each existing user position exactly once

  @functional @S07
  Scenario: GET /users/:id still returns a single user
    When I request the seeded user "manager@test.com" by id from the users API
    Then the API response status should be 200
    And the API response should be the user "manager@test.com"

  @functional @S07
  Scenario: GET /users/projects/:id still returns the project's users
    When I request the users of project 1 from the users API
    Then the API response status should be 200
    And the API response should be a JSON list

  # Non-destructive payloads only (no stacked DROP/DELETE against the shared database).
  # "_" and "\" depend on open question Q1 (the escape character lost from the ticket) and are provisional.
  @functional @security @S08
  Scenario Outline: Special characters and SQL injection strings are searched as plain text
    When I request the users API with search "<search>" and position ""
    Then the API response status should be 200
    And every user in the API response should match search "<search>" and position ""
    And the users API without parameters should still return all seeded users

    Examples:
      | search              |
      | '                   |
      | %                   |
      | ' OR 1=1 --         |
      | ' OR '1'='1         |
      | %' OR email LIKE '% |
      | _                   |
      | \\                  |
