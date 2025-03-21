package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

    private Response response;
    private BreedPage breedPage;
    private CategoryPage categoryPage;
    private String breedName;
    private String breedId;

    public CommonSteps() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(input);
        }

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        breedPage = new BreedPage(apiKey, baseUrl);
        categoryPage = new CategoryPage(apiKey, baseUrl);
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public void setBreedId(String breedId) {
        this.breedId = breedId;
    }

    public Response getResponse() {
        return this.response;
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        Map<String, java.util.function.Supplier<Response>> endpointActions = new HashMap<>();

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