package io.github.earleofberkshire.catapirestassured.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Test runner class for executing Cucumber tests for the Cat API. This class configures Cucumber
 * options for feature file location, step definition package, and reporting plugins.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features", // Path to feature files
    glue = {
      "io.github.earleofberkshire.catapirestassured.stepdefinitions",
      "io.github.earleofberkshire.catapirestassured.context",
      "io.github.earleofberkshire.catapirestassured.api"
    },
    plugin = {"pretty", "html:target/cucumber-reports.html"}, // Reporting plugins
    tags = "@breeds or @categories" // Optional: Add tags to run specific scenarios or features
    )
public class TestRunner {}
