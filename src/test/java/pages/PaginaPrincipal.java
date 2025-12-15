package pages;

import org.openqa.selenium.WebDriver;

public class PaginaPrincipal extends BasePage {

    public PaginaPrincipal(WebDriver driver) {
        super(driver);
    }

    public void navigateToFreeRangeTesters() {
        navigateTo("https://www.freerangetesters.com");
    }
}