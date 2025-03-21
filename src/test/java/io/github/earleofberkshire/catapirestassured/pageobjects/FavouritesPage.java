package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class FavouritesPage extends BasePage {

    public FavouritesPage(String apiKey, String baseUrl) {
        super(apiKey, baseUrl);
    }

//    public Response getAllBreeds() {
//        return given().header("x-api-key", apiKey).when().get("/breeds");
//    }

    // ... other methods for breed-related endpoints
}