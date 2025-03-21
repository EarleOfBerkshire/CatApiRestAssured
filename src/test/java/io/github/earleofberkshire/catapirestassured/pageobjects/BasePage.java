package io.github.earleofberkshire.catapirestassured.pageobjects;

import io.github.earleofberkshire.catapirestassured.api.ApiClient; // Import ApiClient
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BasePage {

    protected ApiClient apiClient; // Declare ApiClient instance

    public BasePage() throws IOException {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(input);
        }

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        this.apiClient = new ApiClient(apiKey, baseUrl); // Initialize ApiClient
    }
}