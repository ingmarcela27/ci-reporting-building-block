package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import pages.BasePage;
import static steps.DriverManager.getDriver;
import static steps.DriverManager.quitDriver;


public class Hooks extends BasePage {

    public Hooks() {
        super(driver);
    }

        @After
        public void afterScenario(Scenario scenario) {
            attachScreenshot(scenario.getName());
            attachScenarioStatus(scenario);
            quitDriver();
        }

        @Attachment(value = "Screenshot - {0}", type = "image/png")
        public byte[] attachScreenshot(String scenarioName) {
            return ((TakesScreenshot) getDriver())
                    .getScreenshotAs(OutputType.BYTES);
        }

        @Attachment(value = "Scenario result")
        public String attachScenarioStatus(Scenario scenario) {
            return "Scenario: " + scenario.getName() +
                    "\nStatus: " + scenario.getStatus();
      }

}



//metodo sacar screenshot solamente a los escenarios fallidos.

// @After
    //public void takeScreenshotOnFailure(Scenario scenario) {
        //if (scenario.isFailed()) {
            //saveScreenshot();
        //}
    //}
//
    //@Attachment(value = "Screenshot on failure", type = "image/png")
    //public byte[] saveScreenshot() {
        //return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    //}