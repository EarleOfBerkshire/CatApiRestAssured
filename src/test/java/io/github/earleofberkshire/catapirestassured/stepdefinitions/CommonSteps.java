package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;

public class CommonSteps {

  private ScenarioContext scenarioContext;
  private BreedPage breedPage;
  private CategoryPage categoryPage;

  public CommonSteps(ScenarioContext scenarioContext) throws IOException {
    this.scenarioContext = scenarioContext;
    this.breedPage = new BreedPage();
    this.categoryPage = new CategoryPage();
  }

  @When("I send a GET request to {string}")
  public void iSendAGETRequestTo(String endpoint) {
    Response response;

    if (endpoint.startsWith("/v1/breeds/search")) {
      response = breedPage.searchBreedsByName(endpoint.substring(endpoint.indexOf("=") + 1));
    } else if (endpoint.startsWith("/v1/breeds/") && !endpoint.equals("/v1/breeds")) {
      response = breedPage.getBreedById(scenarioContext.getBreedId());
    } else if (endpoint.equals("/v1/breeds")) {
      response = breedPage.getAllBreeds();
    } else if (endpoint.equals("/v1/categories")) {
      response = categoryPage.getAllCategories();
    } else {
      throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
    }

    System.out.println("API Response: " + response.getBody().asString());
    scenarioContext.setResponse(response);
  }

  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
    Assertions.assertEquals(expectedStatusCode, scenarioContext.getResponse().getStatusCode(),
            "Expected status code " + expectedStatusCode + " but got " + scenarioContext.getResponse().getStatusCode());
  }
}