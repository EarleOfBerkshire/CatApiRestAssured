@breeds
Feature: Breeds
  Scenario: List all breeds
    When I send a GET request to "/v1/breeds"
    Then the response status code should be 200
    And the response should contain a list of breeds

  Scenario: Search breeds by name
    Given I have a breed name "Bengal"
    When I send a GET request to "/v1/breeds/search?q={breed_name}"
    Then the response status code should be 200
    And the response should contain breeds matching "Bengal"

  Scenario: Retrieve a specific breed by ID
    Given I have a valid breed ID "abys"
    When I send a GET request to "/v1/breeds/{breed_id}"
    Then the response status code should be 200
    And the response should contain breed details for "Abyssinian"

  Scenario: Retrieve breed with invalid ID
    Given I have an invalid breed ID "invalid_id"
    When I send a GET request to "/v1/breeds/{breed_id}"
    Then the response status code should be 400
    And the response should indicate "INVALID_DATA"

  Scenario: Search breeds with empty name
    Given I have a breed name ""
    When I send a GET request to "/v1/breeds/search?q={breed_name}"
    Then the response status code should be 200
    And the response should be an empty array