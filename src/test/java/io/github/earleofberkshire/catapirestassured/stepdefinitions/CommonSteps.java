import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.earleofberkshire.catapirestassured.api.ApiClient;
import io.github.earleofberkshire.catapirestassured.context.ScenarioContext;
import io.github.earleofberkshire.catapirestassured.pageobjects.BreedPage;
import io.github.earleofberkshire.catapirestassured.pageobjects.CategoryPage;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommonSteps {

    private ScenarioContext scenarioContext;
    private BreedPage breedPage;
    private CategoryPage categoryPage;
    private ApiClient apiClient;

    public CommonSteps(ScenarioContext scenarioContext) throws IOException {
        this.scenarioContext = scenarioContext;
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/test/resources/application.properties")) {
            properties.load(input);
        }

        String apiKey = properties.getProperty("api.key");
        String baseUrl = properties.getProperty("base.url");

        this.apiClient = new ApiClient(apiKey, baseUrl);
        this.breedPage = new BreedPage(apiKey, baseUrl);
        this.categoryPage = new CategoryPage(apiKey, baseUrl);
    }

    @When("I send a GET request to {string}")
    public void iSendAGETRequestTo(String endpoint) {
        Response response;

        if (endpoint.startsWith("/v1/breeds/search")) {
            response = apiClient.get(endpoint, null);
        } else if (endpoint.startsWith("/v1/breeds/") && !endpoint.equals("/v1/breeds")) {
            response = apiClient.get("/v1/breeds/{breed_id}".replace("{breed_id}", scenarioContext.getBreedId()), null);
        } else if (endpoint.equals("/v1/breeds")) {
            response = apiClient.get(endpoint, null);
        } else if (endpoint.equals("/v1/categories")) {
            response = apiClient.get(endpoint, null);
        } else {
            throw new IllegalArgumentException("Unsupported endpoint: " + endpoint);
        }

        System.out.println("API Response: " + response.getBody().asString());
        scenarioContext.setResponse(response);
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
        Assertions.assertEquals(expectedStatusCode, scenarioContext.getResponse().getStatusCode(),
                "Expected status code " + expectedStatusCode + " but got " + scenarioContext.getResponse().getStatusCode());
    }
}