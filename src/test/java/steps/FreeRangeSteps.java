package steps;

import io.cucumber.java.en.Given;
import pages.PaginaPrincipal;
import io.qameta.allure.Allure;

import static steps.DriverManager.getDriver;

public class FreeRangeSteps {

    PaginaPrincipal landingPage;

    @Given("I navigate to www.freerangetesters.com")
    public void iNavigateToFRT() {

        // Bloque de codigo para adjuntar logs personalizados al reporte, se puede colocar en cualquier step o paso.
        //Ideal para datos usados, URLs, valores esperados vs actuales.
        Allure.addAttachment(
                "Navigation log",
                "Navigated to FreeRangeTesters homepage successfully"
        );

        landingPage = new PaginaPrincipal(getDriver());
        landingPage.navigateToFreeRangeTesters();
    }
}