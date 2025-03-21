package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.restassured.response.Response;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Assertions;

public class VoteSteps {

    private ScenarioContext scenarioContext;
    private CategoryPage categoryPage;

    public VoteSteps(ScenarioContext scenarioContext) throws IOException {
        this.scenarioContext = scenarioContext;
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream("src/test/resources/application.properties");
        properties.load(input);

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        categoryPage = new CategoryPage();
    }


    @Then("the response should contain a list of votes")
    public void theResponseShouldContainAListOfCategories() {
        Response response = scenarioContext.getResponse();
        Assertions.assertNotNull(response, "Response should not be null");

        List<Object> categories = response.jsonPath().getList("$");

        Assertions.assertNotNull(categories, "Votes list should not be null");
        Assertions.assertFalse(categories.isEmpty(), "Votes list should not be empty");

        for (Object category : categories) {
            if (category instanceof Map) {
                Map<String, ?> categoryMap = (Map<String, ?>) category;
                assertThat(categoryMap, hasKey("id"));
                assertThat(categoryMap, hasKey("image_id"));
                assertThat(categoryMap, hasKey("sub_id"));
                assertThat(categoryMap, hasKey("created_at"));
                assertThat(categoryMap, hasKey("value"));
                assertThat(categoryMap, hasKey("country_code"));
                assertThat(categoryMap, hasKey("image"));
            } else {
                Assertions.fail("Votes object is not a Map");
            }
        }
    }


}