Feature: Votes

  Scenario: List votes (requires API key)
    Given I have a valid API key
    When I send a GET request to "/v1/votes"
    Then the response status code should be 200
    And the response should contain a list of votes

  Scenario: Create a vote (requires API key)
    Given I have a valid API key
    And I have a valid image ID "some_image_id"
    And I have a vote value of 1
    When I send a POST request to "/v1/votes" with image_id and vote value
    Then the response status code should be 201
    And the response should indicate a successful vote

  Scenario: Delete a vote (requires API key)
    Given I have a valid API key
    And I have a valid vote ID "some_vote_id"
    When I send a DELETE request to "/v1/votes/{vote_id}"
    Then the response status code should be 200
    And the response should indicate a successful deletion

  Scenario: Create vote with invalid image ID
    Given I have a valid API key
    And I have an invalid image ID "invalid_image"
    And I have a vote value of 1
    When I send a POST request to "/v1/votes" with image_id and vote value
    Then the response status code should be 400

  Scenario: Create vote with invalid vote value
    Given I have a valid API key
    And I have a valid image ID "some_image_id"
    And I have a vote value of 99
    When I send a POST request to "/v1/votes" with image_id and vote value
    Then the response status code should be 400

  Scenario: Delete non-existent vote
    Given I have a valid API key
    And I have an invalid vote ID "invalid_vote"
    When I send a DELETE request to "/v1/votes/{vote_id}"
    Then the response status code should be 400