package mobile;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

public class RealDeviceTest extends ApkInteractionTest {
    @Override @Test public void entersTextAndChecksOption() throws Exception {
        assertEquals(target, "real", "Run this class with -Dmobile.target=real and a physical device UDID");
        super.entersTextAndChecksOption();
        screenshot("physical-device-apk");
    }
}
