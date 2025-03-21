//package io.github.earleofberkshire.catapirestassured.stepdefinitions;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.*;
//
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import io.github.earleofberkshire.catapirestassured.pageobjects.*;
//import io.restassured.response.Response;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.*;
//
//import org.junit.jupiter.api.Assertions;
//
//public class TheCatApiSteps {
//
//  private Response response;
//  private String breedId;
//  private String breedName;
//  private String categoryId;
//  private String imageId;
//  private String voteId;
//  private String favouriteId;
//  private String apiKey;
//  private String baseUrl;
//
//  private BreedPage breedPage;
//  private CategoryPage categoryPage;
//  private ImagePage imagePage;
//  private FavouritesPage favouritesPage;
//  private VotesPage votesPage;
//  private AnalysisPage analysisPage;
//
//  public TheCatApiSteps() throws IOException {
//    Properties properties = new Properties();
//    FileInputStream input = new FileInputStream("src/test/resources/application.properties");
//    properties.load(input);
//
//    apiKey = properties.getProperty("api.key");
//    baseUrl = properties.getProperty("base.url");
//
//    breedPage = new BreedPage(apiKey, baseUrl);
//    categoryPage = new CategoryPage(apiKey, baseUrl);
//    imagePage = new ImagePage(apiKey, baseUrl);
//    favouritesPage = new FavouritesPage(apiKey, baseUrl);
//    votesPage = new VotesPage(apiKey, baseUrl);
//    analysisPage = new AnalysisPage(apiKey, baseUrl);
//  }
//
//  @When("I send a GET request to {string}")
//  public void iSendAGETRequestTo(String endpoint) {
//    Map<String, java.util.function.Supplier<Response>> endpointActions = new HashMap<>();
//
//    //Breed Endpoints
//    endpointActions.put("/v1/breeds/search", () -> breedPage.searchBreedsByName(breedName));
//    endpointActions.put("/v1/breeds/" + breedId, () -> breedPage.getBreedById(breedId));
//    endpointActions.put("/v1/breeds", breedPage::getAllBreeds);
//
//    //Category Endpoints
//    endpointActions.put("/v1/categories", categoryPage::getAllCategories);
//
//    response = endpointActions.entrySet().stream()
//            .filter(entry -> endpoint.contains(entry.getKey()))
//            .findFirst()
//            .map(Map.Entry::getValue)
//            .map(java.util.function.Supplier::get)
//            .orElseThrow(() -> new IllegalArgumentException("Unsupported endpoint: " + endpoint));
//
//  }
//
//  public void setResponse(Response response) {
//    this.response = response;
//  }
//
//  @Then("the response status code should be {int}")
//  public void theResponseStatusCodeShouldBe(int expectedStatusCode) {
//    Assertions.assertEquals(
//        expectedStatusCode,
//        response.getStatusCode(),
//        "Expected status code " + expectedStatusCode + " but got " + response.getStatusCode());
//  }
//
//  @Then("the response should contain a list of breeds")
//  public void theResponseShouldContainAListOfBreeds() {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<Object> breeds = response.jsonPath().getList("$");
//
//    Assertions.assertNotNull(breeds, "Breeds list should not be null");
//    Assertions.assertFalse(breeds.isEmpty(), "Breeds list should not be empty");
//
//    for (Object breed : breeds) {
//      if (breed instanceof Map) { // Check if the breed object is a map
//        Map<String, ?> breedMap = (Map<String, ?>) breed; // Cast to Map
//        assertThat(breedMap, hasKey("id"));
//        assertThat(breedMap, hasKey("name"));
//      } else {
//        Assertions.fail("Breed object is not a Map");
//      }
//    }
//  }
//
//  @Given("I have a breed name {string}")
//  public void iHaveABreedName(String breedName) {
//    this.breedName = breedName;
//  }
//
//  @Given("I have a valid breed ID {string}")
//  public void iHaveAValidBreedID(String breedId) {
//    this.breedId = breedId;
//  }
//
//  @Given("I have an invalid breed ID {string}")
//  public void iHaveAnInvalidBreedID(String breedId) {
//    this.breedId = breedId;
//  }
//
//  @Then("the response should contain breeds matching {string}")
//  public void theResponseShouldContainBreedsMatching(String expectedBreedName) {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<Map<String, ?>> breeds = response.jsonPath().getList("$");
//
//    Assertions.assertNotNull(breeds, "Breeds list should not be null");
//    Assertions.assertFalse(breeds.isEmpty(), "Breeds list should not be empty");
//
//    boolean breedFound = false;
//
//    for (Map<String, ?> breed : breeds) {
//      String breedName =
//          (String) breed.get("name"); // Assuming the breed name is in the 'name' field
//      if (breedName != null && breedName.toLowerCase().contains(expectedBreedName.toLowerCase())) {
//        breedFound = true;
//        break; // Exit the loop as soon as a match is found
//      }
//    }
//
//    Assertions.assertTrue(
//        breedFound, "No breeds matching '" + expectedBreedName + "' found in the response.");
//  }
//
//  @Then("the response should contain breed details for {string}")
//  public void theResponseShouldContainBreedDetailsFor(String expectedBreedName) {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<Map<String, ?>> breedDetailsList = response.jsonPath().getList("$");
//
//    Assertions.assertNotNull(breedDetailsList, "Breed details list should not be null");
//    Assertions.assertFalse(breedDetailsList.isEmpty(), "Breed details list should not be empty");
//
//    boolean breedFound = false;
//    Map<String, ?> foundBreed = null;
//
//    for (Map<String, ?> breed : breedDetailsList) {
//      String actualBreedName = (String) breed.get("name");
//      if (actualBreedName != null && actualBreedName.equals(expectedBreedName)) {
//        breedFound = true;
//        foundBreed = breed;
//        break;
//      }
//    }
//
//    Assertions.assertTrue(breedFound, "Breed details for '" + expectedBreedName + "' not found in response.");
//
//    if (foundBreed != null) {
//      // Optional: Add more assertions to check other breed details
//      // Example: Assert.assertNotNull(foundBreed.get("temperament"));
//    }
//  }
//
//  @Then("the response should be an empty array")
//  public void theResponseShouldBeAnEmptyArray() {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<?> responseList = response.jsonPath().getList("$"); // Get the response as a list
//
//    Assertions.assertNotNull(responseList, "Response list should not be null");
//    Assertions.assertTrue(responseList.isEmpty(), "Response list should be empty");
//  }
//
//  @Then("the response should indicate {string}")
//  public void theResponseShouldIndicate(String expectedResponse) {
//    String actualResponseBody = response.getBody().asString();
//    Assertions.assertEquals(expectedResponse, actualResponseBody, "Incorrect response found");
//  }
//
//  @Then("the response should contain a list of categories")
//  public void theResponseShouldContainAListOfCategories() {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<Object> categories = response.jsonPath().getList("$");
//
//    Assertions.assertNotNull(categories, "Categories list should not be null");
//    Assertions.assertFalse(categories.isEmpty(), "Categories list should not be empty");
//
//    for (Object category : categories) {
//      if (category instanceof Map) { // Check if the category object is a map
//        Map<String, ?> categoryMap = (Map<String, ?>) category; // Cast to Map
//        assertThat(categoryMap, hasKey("id"));
//        assertThat(categoryMap, hasKey("name"));
//      } else {
//        Assertions.fail("Category object is not a Map");
//      }
//    }
//  }
//
//  @Then("all category IDs should be unique")
//  public void allCategoryIDsShouldBeUnique() {
//    Assertions.assertNotNull(response, "Response should not be null");
//
//    List<Map<String, ?>> categories = response.jsonPath().getList("$");
//
//    Assertions.assertNotNull(categories, "Categories list should not be null");
//    Assertions.assertFalse(categories.isEmpty(), "Categories list should not be empty");
//
//    Set<Integer> categoryIds = new HashSet<>();
//
//    for (Map<String, ?> category : categories) {
//      Integer categoryId = category.get("id") instanceof Integer ? (Integer) category.get("id") : null;
//
//      Assertions.assertNotNull(categoryId, "Category ID should not be null");
//
//      boolean added = categoryIds.add(categoryId);
//      Assertions.assertTrue(added, "Duplicate category ID found: " + categoryId);
//    }
//  }
//
//}
