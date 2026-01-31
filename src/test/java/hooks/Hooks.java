/**
 * Hooks de Cucumber que controlan el ciclo de vida de cada escenario.
 *
 * Esta clase actúa como el puente entre Cucumber y Selenium,
 * asegurando que el WebDriver se inicialice antes de cada escenario
 * y se liberen los recursos al finalizar.
 */

package hooks;

import config.AllureEnvironmentWriter;
import driver.DriverFactory;
import driver.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks {

    private static boolean environmentWritten = false;

    /**
     * Se ejecuta antes de cada escenario.
     * Inicializa el entorno de ejecución y crea el WebDriver.
     */

    @Before
    public void beforeScenario() {
        DriverManager.setDriver(DriverFactory.createDriver());

        if (!environmentWritten) {
            AllureEnvironmentWriter.write();
            environmentWritten = true;
        }
    }

    /**
     * Se ejecuta después de cada escenario.
     * Captura evidencia en caso de fallo y cierra el navegador,
     * garantizando la independencia entre escenarios.
     */

    @After
    public void afterScenario(Scenario scenario) {

        if (scenario.isFailed()) {
            attachScreenshot();
            attachFailureInfo(scenario);
        }

        DriverManager.quitDriver();
    }

    @Attachment(value = "Screenshot on failure", type = "image/png")
    public byte[] attachScreenshot() {
        if (DriverManager.getDriver() == null) {
            return new byte[0];
        }
        return ((TakesScreenshot) DriverManager.getDriver())
                .getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "Failure details", type = "text/plain")
    public String attachFailureInfo(Scenario scenario) {
        return String.format(
                "Scenario: %s%nStatus: %s%nThread: %s%nCI: %s%nBrowser: Chrome",
                scenario.getName(),
                scenario.getStatus(),
                Thread.currentThread().getName(),
                System.getenv("CI")
        );
    }
}
