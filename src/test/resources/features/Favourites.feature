Feature: Favourites
  Scenario: List favourites (requires API key)
    Given I have a valid API key
    When I send a GET request to "/v1/favourites"
    Then the response status code should be 200
    And the response should contain a list of favourites

  Scenario: Add a favourite (requires API key)
    Given I have a valid API key
    And I have a valid image ID "some_image_id"
    When I send a POST request to "/v1/favourites" with image_id
    Then the response status code should be 200
    And the response should indicate success

  Scenario: Delete a favourite (requires API key)
    Given I have a valid API key
    And I have a valid favourite ID "some_favourite_id"
    When I send a DELETE request to "/v1/favourites/{favourite_id}"
    Then the response status code should be 200
    And the response should indicate a successful deletion

  Scenario: Add favourite with invalid image ID
    Given I have a valid API key
    And I have an invalid image ID "invalid_image"
    When I send a POST request to "/v1/favourites" with image_id
    Then the response status code should be 400

  Scenario: Delete non-existent favourite
    Given I have a valid API key
    And I have an invalid favourite ID "invalid_favourite"
    When I send a DELETE request to "/v1/favourites/{favourite_id}"
    Then the response status code should be 400