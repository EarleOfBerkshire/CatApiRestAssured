Feature: Images
  Scenario: Search images with breed ID
    Given I have a valid breed ID "beng"
    When I send a GET request to "/v1/images/search?breed_ids={breed_id}"
    Then the response status code should be 200
    And the response should contain images of the "Bengal" breed

  Scenario: Search images with category ID
    Given I have a valid category ID "1"
    When I send a GET request to "/v1/images/search?category_ids={category_id}"
    Then the response status code should be 200
    And the response should contain images from the specified category

  Scenario: Search images with limit and page
    When I send a GET request to "/v1/images/search?limit=5&page=1"
    Then the response status code should be 200
    And the response should contain 5 images

  Scenario: Get an image by ID
    Given I have a valid image ID "some_image_id"
    When I send a GET request to "/v1/images/{image_id}"
    Then the response status code should be 200
    And the response should contain details for the image

  Scenario: Upload an image (requires file upload and API key)
    Given I have a valid API key
    And I have an image file "cat_image.jpg"
    When I send a POST request to "/v1/images/upload" with the image file
    Then the response status code should be 201
    And the response should indicate a successful upload

  Scenario: Delete an image (requires API key)
    Given I have a valid API key
    And I have a valid image ID "some_image_id"
    When I send a DELETE request to "/v1/images/{image_id}"
    Then the response status code should be 200
    And the response should indicate a successful deletion

  Scenario: Search images with invalid breed ID
    Given I have an invalid breed ID "invalid_breed"
    When I send a GET request to "/v1/images/search?breed_ids={breed_id}"
    Then the response status code should be 200
    And the response should be an empty array

  Scenario: Search images with invalid category ID
    Given I have an invalid category ID "9999"
    When I send a GET request to "/v1/images/search?category_ids={category_id}"
    Then the response status code should be 200
    And the response should be an empty array

  Scenario: Search images with limit 0
    When I send a GET request to "/v1/images/search?limit=0"
    Then the response status code should be 200
    And the response should be an empty array

  Scenario: Search images with large limit
    When I send a GET request to "/v1/images/search?limit=100"
    Then the response status code should be 200
    And the response should contain 100 images

  Scenario: Get image with invalid ID
    Given I have an invalid image ID "invalid_image"
    When I send a GET request to "/v1/images/{image_id}"
    Then the response status code should be 404

  Scenario: Upload image with invalid API key
    Given I have an invalid API key
    And I have an image file "cat_image.jpg"
    When I send a POST request to "/v1/images/upload" with the image file
    Then the response status code should be 401

  Scenario: Upload non-image file
    Given I have a valid API key
    And I have a file "text_file.txt"
    When I send a POST request to "/v1/images/upload" with the file
    Then the response status code should be 400

  Scenario: Delete image with invalid API key
    Given I have an invalid API key
    And I have a valid image ID "some_image_id"
    When I send a DELETE request to "/v1/images/{image_id}"
    Then the response status code should be 401