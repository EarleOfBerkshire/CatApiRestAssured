package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BreedPage extends BasePage {

  public BreedPage(String apiKey, String baseUrl) {
    super(apiKey, baseUrl);
  }

  public Response getAllBreeds() {
    return given().header("x-api-key", apiKey).when().get("/breeds");
  }

  public Response getBreedById(String breedId) {
    return given().header("x-api-key", apiKey).when().get("/breeds/" + breedId);
  }

  public Response searchBreedsByName(String breedName) {
    return given()
        .header("x-api-key", apiKey)
        .when()
        .get("/breeds/search?attach_image=1&q=" + breedName);
  }
}
