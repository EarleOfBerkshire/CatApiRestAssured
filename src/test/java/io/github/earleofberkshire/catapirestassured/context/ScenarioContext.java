package io.github.earleofberkshire.catapirestassured.context;

import io.restassured.response.Response;

public class ScenarioContext {

    private Response response;
    private String breedName;
    private String breedId;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public String getBreedName() {
        return breedName;
    }

    public void setBreedName(String breedName) {
        this.breedName = breedName;
    }

    public String getBreedId() {
        return breedId;
    }

    public void setBreedId(String breedId) {
        this.breedId = breedId;
    }
}