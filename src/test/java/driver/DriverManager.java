/**
 * Gestiona la instancia activa de WebDriver.
 *
 * Utiliza ThreadLocal para garantizar seguridad en ejecuciones
 * paralelas y evitar conflictos entre escenarios.
 */

package driver;

import org.openqa.selenium.WebDriver;

public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    public static WebDriver getDriver() {
        return driver.get();
    }

    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    public static void quitDriver() {
        WebDriver currentDriver = driver.get();
        if (currentDriver != null) {
            currentDriver.quit();
            driver.remove();
        }
    }
}