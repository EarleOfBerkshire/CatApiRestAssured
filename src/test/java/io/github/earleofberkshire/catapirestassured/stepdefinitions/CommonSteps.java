package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class CommonSteps {

    private BreedPage breedPage;
    private CategoryPage categoryPage;
    private ScenarioContext scenarioContext;

    public CommonSteps(ScenarioContext scenarioContext) throws IOException { // Make sure this is public
        this.scenarioContext = scenarioContext;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(input);
        }

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        breedPage = new BreedPage(apiKey, baseUrl);
        categoryPage = new CategoryPage(apiKey, baseUrl);
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        Map<String, java.util.function.Supplier<Response>> endpointActions = new HashMap<>();

        // Breed Endpoints
        endpointActions.put("/v1/breeds/search", () -> breedPage.searchBreedsByName(scenarioContext.getBreedName()));
        endpointActions.put("/v1/breeds/{breed_id}", () -> breedPage.getBreedById(scenarioContext.getBreedId()));
        endpointActions.put("/v1/breeds", breedPage::getAllBreeds);

        // Category Endpoints
        endpointActions.put("/v1/categories", categoryPage::getAllCategories);

        // Construct the actual endpoint for the lambda.
        String lambdaEndpoint;

        // Check for breed-specific requests, but exclude search requests.
        if (endpoint.startsWith("/v1/breeds/") && endpoint.length() > "/v1/breeds/".length() && !endpoint.contains("search")) {
            lambdaEndpoint = "/v1/breeds/{breed_id}"; //Use the endpoint with the placeholder.
        } else if (endpoint.startsWith("/v1/breeds/search")) {
            lambdaEndpoint = "/v1/breeds/search"; //use the search endpoint.
        } else {
            lambdaEndpoint = endpoint;
        }

        Response response = endpointActions.entrySet().stream()
                .filter(entry -> entry.getKey().equals(lambdaEndpoint)) //use .equals to ensure exact match.
                .findFirst()
                .map(Map.Entry::getValue)
                .map(java.util.function.Supplier::get)
                .orElseThrow(() -> new IllegalArgumentException("Unsupported endpoint: " + endpoint));

        System.out.println("*_*_**_*_*_*_*_*_*_ request url: " + endpoint);
        System.out.println("API Response: " + response.getBody().asString());

        scenarioContext.setResponse(response);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assertions.assertEquals(
                expectedStatusCode,
                scenarioContext.getResponse().getStatusCode(),
                "Expected status code " + expectedStatusCode + " but got " + scenarioContext.getResponse().getStatusCode());
    }
}