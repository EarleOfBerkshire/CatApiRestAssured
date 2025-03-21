package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.response.Response;
import java.io.IOException;

public class CategoryPage extends BasePage {

    public CategoryPage() throws IOException {
        super();
    }

    public Response getAllCategories() {
        return apiClient.get("/v1/categories", null);
    }
}