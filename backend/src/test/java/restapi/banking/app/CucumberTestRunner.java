package restapi.banking.app;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "restapi.banking.app.steps",
        plugin = {"pretty", "json:target/cucumber-reports/Cucumber.json"},
        monochrome = true)
public class CucumberTestRunner {
}