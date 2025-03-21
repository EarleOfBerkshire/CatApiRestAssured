package io.github.earleofberkshire.catapirestassured.stepdefinitions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.en.Then;
import io.restassured.response.Response;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.Assertions;

public class CategorySteps {

    private Response response;
    private CategoryPage categoryPage;

    public CategorySteps() throws IOException {
        Properties properties = new Properties();
        FileInputStream input = new FileInputStream("src/test/resources/application.properties");
        properties.load(input);

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        categoryPage = new CategoryPage(apiKey, baseUrl);
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    @Then("the response should contain a list of categories")
    public void theResponseShouldContainAListOfCategories() {
        Assertions.assertNotNull(response, "Response should not be null");

        List<Object> categories = response.jsonPath().getList("$");

        Assertions.assertNotNull(categories, "Categories list should not be null");
        Assertions.assertFalse(categories.isEmpty(), "Categories list should not be empty");

        for (Object category : categories) {
            if (category instanceof Map) { // Check if the category object is a map
                Map<String, ?> categoryMap = (Map<String, ?>) category; // Cast to Map
                assertThat(categoryMap, hasKey("id"));
                assertThat(categoryMap, hasKey("name"));
            } else {
                Assertions.fail("Category object is not a Map");
            }
        }
    }

    @Then("all category IDs should be unique")
    public void allCategoryIDsShouldBeUnique() {
        Assertions.assertNotNull(response, "Response should not be null");

        List<Map<String, ?>> categories = response.jsonPath().getList("$");

        Assertions.assertNotNull(categories, "Categories list should not be null");
        Assertions.assertFalse(categories.isEmpty(), "Categories list should not be empty");

        Set<Integer> categoryIds = new HashSet<>();

        for (Map<String, ?> category : categories) {
            Integer categoryId = category.get("id") instanceof Integer ? (Integer) category.get("id") : null;

            Assertions.assertNotNull(categoryId, "Category ID should not be null");

            boolean added = categoryIds.add(categoryId);
            Assertions.assertTrue(added, "Duplicate category ID found: " + categoryId);
        }
    }

}