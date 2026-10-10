package mobile;

import org.openqa.selenium.*;
import org.testng.annotations.Test;
import java.util.Map;
import static org.testng.Assert.*;

public class SlideGestureTest extends BaseTest {
    @Test public void slideChangesSeekBarProgress() throws Exception {
        openViews("Seek Bar");
        WebElement seekBar = visible(By.id("io.appium.android.apis:id/seek"));
        WebElement progress = visible(By.id("io.appium.android.apis:id/progress"));
        Rectangle bounds = seekBar.getRect();
        // The label starts empty; set the thumb to zero before measuring the slide.
        driver.executeScript("mobile: clickGesture", Map.of(
                "x", bounds.x + 12, "y", bounds.y + bounds.height / 2));
        wait.until(d -> progress.getText().startsWith("0 "));
        int beforeValue = Integer.parseInt(progress.getText().split(" ")[0]);
        screenshot("slide-before");
        // Sliding the thumb horizontally changes the displayed progress value.
        driver.executeScript("mobile: dragGesture", Map.of(
                "startX", bounds.x + 12, "startY", bounds.y + bounds.height / 2,
                "endX", bounds.x + (int) (bounds.width * 0.8),
                "endY", bounds.y + bounds.height / 2, "speed", 600));
        wait.until(d -> Integer.parseInt(progress.getText().split(" ")[0]) > beforeValue);
        assertTrue(Integer.parseInt(progress.getText().split(" ")[0]) > beforeValue);
        screenshot("slide-after");
    }
}
