package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class CommonSteps {

    private Response response;
    private BreedPage breedPage;
    private CategoryPage categoryPage;
    private String breedId;
    private String breedName;

    public void setResponse(Response response) {
        this.response = response;
    }
    @When("I send a GET request to {string}")
  public void iSendAGETRequestTo(String endpoint) {
    Map<String, Supplier<Response>> e ndpointActions = new HashMap<>();

    //Breed Endpoints
    endpointActions.put("/v1/breeds/search", () -> breedPage.searchBreedsByName(breedName));
    endpointActions.put("/v1/breeds/" + breedId, () -> breedPage.getBreedById(breedId));
    endpointActions.put("/v1/breeds", breedPage::getAllBreeds);

    //Category Endpoints
    endpointActions.put("/v1/categories", categoryPage::getAllCategories);

    response = endpointActions.entrySet().stream()
            .filter(entry -> endpoint.contains(entry.getKey()))
            .findFirst()
            .map(Map.Entry::getValue)
            .map(java.util.function.Supplier::get)
            .orElseThrow(() -> new IllegalArgumentException("Unsupported endpoint: " + endpoint));

  }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assertions.assertEquals(
                expectedStatusCode,
                response.getStatusCode(),
                "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
    }
}