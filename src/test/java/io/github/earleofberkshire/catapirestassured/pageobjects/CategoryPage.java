package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class CategoryPage extends BasePage {

    public CategoryPage(String apiKey, String baseUrl) {
        super(apiKey, baseUrl);
    }

    public Response getAllCategories() {
        return given().header("x-api-key", apiKey).when().get("/categories");
    }

}