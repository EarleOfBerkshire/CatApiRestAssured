package io.github.earleofberkshire.catapirestassured.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class ApiClient {

    private String apiKey;
    private String baseUrl;

    public ApiClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public Response get(String endpoint, Map<String, ?> queryParams) {
        RequestSpecification request = RestAssured.given()
                .header("x-api-key", apiKey)
                .baseUri(baseUrl);

        if (queryParams != null && !queryParams.isEmpty()) {
            request = request.queryParams(queryParams);
        }

        return request.get(endpoint);
    }
}