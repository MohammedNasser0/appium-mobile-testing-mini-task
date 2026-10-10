package mobile;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class ApkInteractionTest extends BaseTest {
    @Test public void entersTextAndChecksOption() throws Exception {
        WebElement checkbox = controlCheckbox();
        // Resource IDs identify the input and checkbox directly.
        WebElement input = visible(By.id("io.appium.android.apis:id/edit"));
        input.sendKeys("Appium mini task");
        driver.hideKeyboard();
        checkbox.click();
        assertEquals(input.getText(), "Appium mini task");
        assertEquals(checkbox.getAttribute("checked"), "true");
        screenshot("apk-interaction");
    }
}
