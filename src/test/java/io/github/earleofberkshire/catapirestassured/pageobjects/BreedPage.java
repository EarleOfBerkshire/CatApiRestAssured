package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.restassured.response.Response;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;

public class BreedPage extends BasePage {

  public BreedPage() throws IOException {
    super();
  }

  public Response getAllBreeds() {
    return apiClient.get("/v1/breeds", null);
  }

  public Response searchBreedsByName(String breedName) {
    return apiClient.get("/v1/breeds/search?q=" + breedName, null);
  }

  public Response getBreedById(String breedId) {
    return apiClient.get("/v1/breeds/" + breedId, null);
  }

  public Response getBreedsSearch(String endpoint) {
    return apiClient.get(endpoint, null);
  }
}