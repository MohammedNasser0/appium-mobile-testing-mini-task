package mobile;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.Map;
import static org.testng.Assert.assertEquals;

public class TapGestureTest extends BaseTest {
    @Test public void tapChecksVisibleCheckbox() throws Exception {
        WebElement checkbox = controlCheckbox();
        assertEquals(checkbox.getAttribute("checked"), "false");
        screenshot("tap-before");
        driver.executeScript("mobile: clickGesture", Map.of("elementId", elementId(checkbox)));
        wait.until(d -> "true".equals(checkbox.getAttribute("checked")));
        screenshot("tap-after");
    }
}
