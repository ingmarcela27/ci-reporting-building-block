/**
 * Runner principal de Cucumber.
 *
 * Esta clase define el punto de entrada para la ejecución de los escenarios BDD.
 * Se encarga de:
 * - Localizar los archivos feature
 * - Enlazar los step definitions
 * - Configurar plugins de reporte (Allure)
 *
 * No contiene lógica de negocio ni de automatización.
 * Su única responsabilidad es orquestar la ejecución.
 */

package runner;

import org.junit.platform.suite.api.*;
import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;
import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectPackages("features")
@ConfigurationParameter(
        key = GLUE_PROPERTY_NAME,
        value = "steps,hooks"
)
@ConfigurationParameter(
        key = PLUGIN_PROPERTY_NAME,
        value = "pretty,io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"
)

public class CucumberTest {
}

