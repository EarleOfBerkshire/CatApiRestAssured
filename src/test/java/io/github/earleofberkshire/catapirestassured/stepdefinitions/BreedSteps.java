package io.github.earleofberkshire.catapirestassured.stepdefinitions;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.Assertions;

public class BreedSteps {

  private ScenarioContext scenarioContext;
  private BreedPage breedPage;

  public BreedSteps(ScenarioContext scenarioContext) throws IOException { // Make sure this is public
    this.scenarioContext = scenarioContext;
    Properties properties = new Properties();
    FileInputStream input = new FileInputStream("src/test/resources/application.properties");
    properties.load(input);

    String apiKey = properties.getProperty("api.key");
    String baseUrl = properties.getProperty("base.url");

    breedPage = new BreedPage(apiKey, baseUrl);
  }


  @Given("I have a breed name {string}")
  public void iHaveABreedName(String breedName) {
    scenarioContext.setBreedName(breedName);
  }

  @Given("I have a valid breed ID {string}")
  public void iHaveAValidBreedID(String breedId) {
    scenarioContext.setBreedId(breedId);
  }

  @Given("I have an invalid breed ID {string}")
  public void iHaveAnInvalidBreedID(String breedId) {
    scenarioContext.setBreedId(breedId);
  }

  @Then("the response should contain a list of breeds")
  public void theResponseShouldContainAListOfBreeds() {
    Response response = scenarioContext.getResponse();
    Assertions.assertNotNull(response, "Response should not be null");

    List<Object> breeds = response.jsonPath().getList("$");

    Assertions.assertNotNull(breeds, "Breeds list should not be null");
    Assertions.assertFalse(breeds.isEmpty(), "Breeds list should not be empty");

    for (Object breed : breeds) {
      if (breed instanceof Map) { // Check if the breed object is a map
        Map<String, ?> breedMap = (Map<String, ?>) breed; // Cast to Map
        assertThat(breedMap, hasKey("id"));
        assertThat(breedMap, hasKey("name"));
      } else {
        Assertions.fail("Breed object is not a Map");
      }
    }
  }

  @Then("the response should contain breeds matching {string}")
  public void theResponseShouldContainBreedsMatching(String expectedBreedName) {
    Response response = scenarioContext.getResponse();
    Assertions.assertNotNull(response, "Response should not be null");

    Object responseBody = response.jsonPath().get("$");

    if (responseBody instanceof List) {
      List<Map<String, ?>> breeds = (List<Map<String, ?>>) responseBody;

      Assertions.assertNotNull(breeds, "Breeds list should not be null");
      Assertions.assertFalse(breeds.isEmpty(), "Breeds list should not be empty");

      boolean breedFound = false;

      for (Map<String, ?> breed : breeds) {
        String breedName = (String) breed.get("name");
        if (breedName != null && breedName.toLowerCase().contains(expectedBreedName.toLowerCase())) {
          breedFound = true;
          break;
        }
      }

      Assertions.assertTrue(breedFound, "No breeds matching '" + expectedBreedName + "' found in the response.");
    } else if (responseBody instanceof Map) {
      Map<String, ?> breed = (Map<String, ?>) responseBody;
      String breedName = (String) breed.get("name");

      Assertions.assertNotNull(breedName, "Breed name should not be null");
      Assertions.assertTrue(breedName.toLowerCase().contains(expectedBreedName.toLowerCase()), "Breed name does not match expected breed name.");
    } else {
      Assertions.fail("Unexpected response type.");
    }
  }

  @Then("the response should contain breed details for {string}")
  public void theResponseShouldContainBreedDetailsFor(String expectedBreedName) {
    Response response = scenarioContext.getResponse();
    Assertions.assertNotNull(response, "Response should not be null");

    Object responseBody = response.jsonPath().get("$");

    if (responseBody instanceof List) {
      // Handle list of breed details (e.g., from search results)
      List<Map<String, ?>> breedDetailsList = (List<Map<String, ?>>) responseBody;

      Assertions.assertNotNull(breedDetailsList, "Breed details list should not be null");
      Assertions.assertFalse(breedDetailsList.isEmpty(), "Breed details list should not be empty");

      boolean breedFound = false;
      Map<String, ?> foundBreed = null;

      for (Map<String, ?> breed : breedDetailsList) {
        String actualBreedName = (String) breed.get("name");
        if (actualBreedName != null && actualBreedName.equals(expectedBreedName)) {
          breedFound = true;
          foundBreed = breed;
          break;
        }
      }

      Assertions.assertTrue(breedFound, "Breed details for '" + expectedBreedName + "' not found in response.");

      if (foundBreed != null) {
        // Optional: Add more assertions to check other breed details
        // Example: Assert.assertNotNull(foundBreed.get("temperament"));
      }
    } else if (responseBody instanceof Map) {
      // Handle single breed object (e.g., from breed ID lookup)
      Map<String, ?> breed = (Map<String, ?>) responseBody;
      String actualBreedName = (String) breed.get("name");

      Assertions.assertNotNull(actualBreedName, "Breed name should not be null");
      Assertions.assertEquals(expectedBreedName, actualBreedName, "Breed name does not match expected breed name.");

      // Optional: Add more assertions to check other breed details
      // Example: Assert.assertNotNull(breed.get("temperament"));
    } else {
      Assertions.fail("Unexpected response type.");
    }
  }

  @Then("the response should be an empty array")
  public void theResponseShouldBeAnEmptyArray() {
    Response response = scenarioContext.getResponse();
    Assertions.assertNotNull(response, "Response should not be null");

    List<?> responseList = response.jsonPath().getList("$"); // Get the response as a list

    Assertions.assertNotNull(responseList, "Response list should not be null");
    Assertions.assertTrue(responseList.isEmpty(), "Response list should be empty");
  }

  @Then("the response should indicate {string}")
  public void theResponseShouldIndicate(String expectedResponse) {
    Response response = scenarioContext.getResponse();
    String actualResponseBody = response.getBody().asString();
    Assertions.assertEquals(expectedResponse, actualResponseBody, "Incorrect response found");
  }
}