package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.VotesPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import java.io.IOException;

public class CommonSteps {

  private ScenarioContext scenarioContext;
  private BreedPage breedPage;
  private CategoryPage categoryPage;
  private VotesPage votesPage;

  public CommonSteps(ScenarioContext scenarioContext) throws IOException {
    this.scenarioContext = scenarioContext;
    this.breedPage = new BreedPage();
    this.categoryPage = new CategoryPage();
    this.votesPage = new VotesPage();
  }

  @When("I send a GET request to {string}")
  public void iSendAGETRequestTo(String endpoint) {
    Response response;

    if (endpoint.startsWith("/breeds/search")) {
      response = breedPage.searchBreedsByName(scenarioContext.getBreedName());
    } else if (endpoint.startsWith("/breeds/") && !endpoint.equals("/breeds")) {
      response = breedPage.getBreedById(scenarioContext.getBreedId());
    } else if (endpoint.equals("/breeds")) {
      response = breedPage.getAllBreeds();
    } else if (endpoint.equals("/categories")) {
      response = categoryPage.getAllCategories();
    } else if (endpoint.equals("/votes")) {
      response = votesPage.getAllVotes();
    } else {
      throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
    }

    System.out.println("API Response: " + response.getBody().asString());
    scenarioContext.setResponse(response);
  }

  @Then("the response status code should be {int}")
  public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
    Assertions.assertEquals(
        expectedStatusCode,
        scenarioContext.getResponse().getStatusCode(),
        "Expected status code "
            + expectedStatusCode
            + " but got "
            + scenarioContext.getResponse().getStatusCode());
  }
}
