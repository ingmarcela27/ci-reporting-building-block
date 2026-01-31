/**
 * Fábrica responsable de crear y configurar instancias de WebDriver para las pruebas.
 *
 * - Centraliza la configuración de ChromeDriver (uso de WebDriverManager y ChromeOptions).
 * - Activa opciones específicas para ejecuciones en CI (modo headless, flags de estabilidad y tamaño de ventana)
 *   cuando la variable de entorno `CI` está en "true".
 * - Devuelve una nueva instancia de `ChromeDriver` preparada para ejecución local o en CI.
 *
 * Beneficios: desacopla la creación del driver del resto del framework, facilita cambios globales
 * de configuración y permite extender la fábrica para soportar otros navegadores o ejecuciones remotas.
 */

package driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class DriverFactory {

    static {
        WebDriverManager.chromedriver().setup();
    }

    public static WebDriver createDriver() {

        ChromeOptions options = new ChromeOptions();

        boolean isCI = "true".equalsIgnoreCase(System.getenv("CI"));

        if (isCI) {
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--disable-gpu");
            options.addArguments("--disable-software-rasterizer");
            options.addArguments("--disable-extensions");
            options.addArguments("--disable-infobars");
            options.addArguments("--disable-notifications");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--window-size=1920,1080");
        }

        return new ChromeDriver(options);
    }
}
