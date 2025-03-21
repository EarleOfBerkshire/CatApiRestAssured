Feature: Analysis

  Scenario: Get analysis of an image (requires API key)
    Given I have a valid API key
    And I have a valid image ID "some_image_id"
    When I send a GET request to "/v1/images/{image_id}/analysis"
    Then the response status code should be 200
    And the response should contain analysis details

  Scenario: Get analysis with invalid image ID
    Given I have a valid API key
    And I have an invalid image ID "invalid_image"
    When I send a GET request to "/v1/images/{image_id}/analysis"
    Then the response status code should be 404