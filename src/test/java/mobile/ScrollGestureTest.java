package mobile;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import java.util.Map;
import static org.testng.Assert.*;

public class ScrollGestureTest extends BaseTest {
    @Test public void scrollRevealsWebViewRow() throws Exception {
        visible(AppiumBy.accessibilityId("Views")).click();
        By webView = AppiumBy.accessibilityId("WebView");
        assertTrue(driver.findElements(webView).isEmpty(), "WebView must start below the viewport");
        WebElement list = visible(By.id("android:id/list"));
        screenshot("scroll-before");
        for (int attempt = 0; attempt < 12 && driver.findElements(webView).isEmpty(); attempt++) {
            boolean more = Boolean.TRUE.equals(driver.executeScript("mobile: scrollGesture",
                    Map.of("elementId", elementId(list), "direction", "down", "percent", 0.8)));
            if (!more) break;
        }
        assertTrue(visible(webView).isDisplayed());
        screenshot("scroll-after");
    }
}
