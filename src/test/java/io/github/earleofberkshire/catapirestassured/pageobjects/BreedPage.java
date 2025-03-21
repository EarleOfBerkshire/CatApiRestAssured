package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;
import java.io.IOException;

public class BreedPage extends BasePage {

  public BreedPage() throws IOException {
    super();
  }

  public Response getAllBreeds() {
    return apiClient.get("/breeds", null);
  }

  public Response searchBreedsByName(String breedName) {
    return apiClient.get("/breeds/search?q=" + breedName, null);
  }

  public Response getBreedById(String breedId) {
    return apiClient.get("/breeds/" + breedId, null);
  }
}