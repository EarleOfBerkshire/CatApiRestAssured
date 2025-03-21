@categories
Feature: Categories

  Scenario: List all categories
    When I send a GET request to "/v1/categories"
    Then the response status code should be 200
    And the response should contain a list of categories
    And all category IDs should be unique
