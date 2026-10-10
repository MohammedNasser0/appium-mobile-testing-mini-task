package mobile;

import org.openqa.selenium.*;
import org.testng.annotations.Test;
import java.util.Map;
import static org.testng.Assert.assertEquals;

public class DragAndDropTest extends BaseTest {
    @Test public void dragDotOntoTarget() throws Exception {
        openViews("Drag and Drop");
        WebElement source = visible(By.id("io.appium.android.apis:id/drag_dot_1"));
        WebElement destination = visible(By.id("io.appium.android.apis:id/drag_dot_2"));
        Rectangle bounds = destination.getRect();
        screenshot("drag-before");
        driver.executeScript("mobile: dragGesture", Map.of("elementId", elementId(source),
                "endX", bounds.x + bounds.width / 2, "endY", bounds.y + bounds.height / 2,
                "speed", 500));
        WebElement result = visible(By.id("io.appium.android.apis:id/drag_result_text"));
        wait.until(d -> result.getText().equals("Dropped!"));
        assertEquals(result.getText(), "Dropped!");
        screenshot("drag-after");
    }
}
