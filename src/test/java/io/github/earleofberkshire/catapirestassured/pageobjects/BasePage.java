package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.restassured.RestAssured;

public class BasePage {

    protected String apiKey;
    protected String baseUrl;

    public BasePage(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        RestAssured.baseURI = baseUrl;
    }
}