package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;

import java.io.IOException;

public class VotesPage extends BasePage {

  public VotesPage() throws IOException {
    super();
  }

    public Response getAllVotes() {
      return apiClient.get("/votes", null);
    }

}
