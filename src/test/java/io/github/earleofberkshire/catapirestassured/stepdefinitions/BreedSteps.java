package io.github.earleofberkshire.catapirestaassured.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Assertions;

public class BreedSteps {

  private ScenarioContext scenarioContext;
  private BreedPage breedPage;

  public BreedSteps(ScenarioContext scenarioContext) throws IOException {
    this.scenarioContext = scenarioContext;
    Properties properties = new Properties();
    FileInputStream input = new FileInputStream("src/test/resources/application.properties");
    properties.load(input);

    String apiKey = properties.getProperty("api.key");
    String baseUrl = properties.getProperty("base.url");

    breedPage = new BreedPage(apiKey, baseUrl);
  }

  @When("I send a GET request to {string}")
  public void iSendAGETRequestTo(String endpoint) {
    Map<String, java.util.function.Supplier<Response>> endpointActions = new HashMap<>();

    //Breed Endpoints
    endpointActions.put("/v1/breeds/search", () -> breedPage.searchBreedsByName(scenarioContext.getBreedName()));
    endpointActions.put("/v1/breeds/" + scenarioContext.getBreedId(), () -> breedPage.getBreedById(scenarioContext.getBreedId()));
    endpointActions.put("/v1/breeds", breedPage::getAllBreeds);

    Response response = endpointActions.entrySet().stream()
            .filter(entry -> endpoint.contains(entry.getKey()))
            .findFirst()
            .map(Map.Entry::getValue)
            .map(java.util.function.Supplier::get)
            .orElseThrow(() -> new IllegalArgumentException("Unsupported endpoint: " + endpoint));

    scenarioContext.setResponse(response);
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

    List<Map<String, ?>> breeds = response.jsonPath().getList("$");

    Assertions.assertNotNull(breeds, "Breeds list should not be null");
    Assertions.assertFalse(breeds.isEmpty(), "Breeds list should not be empty");

    boolean breedFound = false;

    for (Map<String, ?> breed : breeds) {
      String breedName =
              (String) breed.get("name"); // Assuming the breed name is in the 'name' field
      if (breedName != null && breedName.toLowerCase().contains(expectedBreedName.toLowerCase())) {
        breedFound = true;
        break; // Exit the loop as soon as a match is found
      }
    }

    Assertions.assertTrue(
            breedFound, "No breeds matching '" + expectedBreedName + "' found in the response.");
  }

  @Then("the response should contain breed details for {string}")
  public void theResponseShouldContainBreedDetailsFor(String expectedBreedName) {
    Response response = scenarioContext.getResponse();
    Assertions.assertNotNull(response, "Response should not be null");

    List<Map<String, ?>> breedDetailsList = response.jsonPath().getList("$");

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