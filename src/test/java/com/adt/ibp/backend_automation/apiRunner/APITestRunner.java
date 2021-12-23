package com.adt.ibp.backend_automation.apiRunner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "./src/test/resources/apiFeatures",
        glue={"com.adt.ibp.stepDefinitions"},
        dryRun = false,
        monochrome = true,
        tags = { "@LoginWithJWT" },
        plugin = { "pretty", "html:target/cucumber-html-report", "json:target/cucumber.json" }
)
public class APITestRunner {
}
