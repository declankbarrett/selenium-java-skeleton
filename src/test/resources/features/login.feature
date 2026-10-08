@login
Feature: Login
  As a registered user
  I want to log into the application
  So that I can access my account

  @smoke
  Scenario: Successful Login
    Given I am on the login page
    When I enter valid credentials
    Then I should be logged in successfully

  @regression
  Scenario Outline: Unsuccessful login shows an error
    Given I am on the login page
    When I log in with username "<username>" and password "<password>"
    Then I should see the login error "<error>"

    Examples:
      | username        | password       | error                                  |
      | locked_out_user | secret_sauce   | Sorry, this user has been locked out.  |
      | standard_user   | wrong_password | Username and password do not match     |
