package mobile;

import org.openqa.selenium.By;
import org.testng.annotations.Test;
import static org.testng.Assert.assertTrue;

public class BuiltInSettingsTest extends BaseTest {
    @Override protected String appMode() { return "settings"; }

    @Test public void opensNetworkSettings() throws Exception {
        // XPath matches the visible Settings row without relying on its layout position.
        visible(By.xpath("//*[@text='Network & internet']")).click();
        assertTrue(visible(By.xpath("//*[@text='Internet']")).isDisplayed());
        screenshot("built-in-settings");
    }
}
