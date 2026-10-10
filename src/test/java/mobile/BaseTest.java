package mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.net.URI;
import java.nio.file.*;
import java.time.Duration;

public abstract class BaseTest {
    protected AndroidDriver driver;
    protected WebDriverWait wait;
    protected String target;

    protected String appMode() { return "apk"; }

    @BeforeMethod
    public void startDriver() throws Exception {
        String udid = System.getProperty("mobile.udid", System.getenv("ANDROID_UDID"));
        if (udid == null || udid.isBlank()) {
            throw new IllegalArgumentException("Set -Dmobile.udid or ANDROID_UDID to an adb device serial.");
        }
        target = System.getProperty("mobile.target", "emulator");
        if (!target.equals("emulator") && !target.equals("real")) {
            throw new IllegalArgumentException("mobile.target must be emulator or real");
        }
        Process check = new ProcessBuilder("adb", "-s", udid, "shell", "getprop", "ro.kernel.qemu")
                .redirectErrorStream(true).start();
        if (!check.waitFor(15, java.util.concurrent.TimeUnit.SECONDS)) {
            check.destroyForcibly();
            throw new IllegalStateException("adb device check timed out");
        }
        String qemu = new String(check.getInputStream().readAllBytes()).trim();
        if (check.exitValue() != 0) throw new IllegalStateException("adb could not reach the selected device");
        boolean emulator = qemu.equals("1");
        if (emulator != target.equals("emulator")) {
            throw new IllegalArgumentException("Selected device does not match mobile.target=" + target);
        }

        // W3C capabilities: Android + UiAutomator2, explicitly targeting one device.
        UiAutomator2Options options = new UiAutomator2Options()
                .setPlatformName("Android").setDeviceName(target + " Android")
                .setUdid(udid).setNewCommandTimeout(Duration.ofSeconds(120));
        if (appMode().equals("settings")) {
            options.setAppPackage("com.android.settings")
                    .setAppActivity("com.android.settings.Settings").setNoReset(true);
            // Restart at the homepage while preserving the built-in app's data.
            options.setCapability("appium:forceAppLaunch", true);
        } else if (appMode().equals("chrome")) {
            options.withBrowserName("chrome");
            String chromedriver = System.getProperty("mobile.chromedriver");
            if (chromedriver != null && !chromedriver.isBlank()) {
                options.setChromedriverExecutable(Path.of(chromedriver).toAbsolutePath().toString());
            }
            options.setCapability("goog:chromeOptions", java.util.Map.of("args",
                    java.util.List.of("--disable-fre", "--no-default-browser-check")));
        } else {
            Path apk = Path.of(System.getProperty("mobile.apk", "apps/ApiDemos-debug.apk")).toAbsolutePath();
            if (!Files.exists(apk)) throw new IllegalArgumentException("Download the APK first: " + apk);
            options.setApp(apk.toString()).setAppPackage("io.appium.android.apis")
                    .setAppActivity("io.appium.android.apis.ApiDemos");
            if (target.equals("real")) {
                // Some phones block shell pm clear; restart API Demos without clearing its data.
                options.setNoReset(true);
                options.setCapability("appium:forceAppLaunch", true);
            }
        }
        driver = new AndroidDriver(URI.create(System.getProperty("mobile.server", "http://127.0.0.1:4723")).toURL(), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void openViews(String screen) {
        // API Demos menu labels expose Android content descriptions (accessibility IDs).
        visible(AppiumBy.accessibilityId("Views")).click();
        driver.findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(new UiSelector().text(\"" + screen + "\"))"));
        visible(AppiumBy.accessibilityId(screen)).click();
    }

    protected WebElement controlCheckbox() {
        openViews("Controls");
        visible(AppiumBy.accessibilityId("1. Light Theme")).click();
        return visible(By.id("io.appium.android.apis:id/check1"));
    }

    protected String elementId(WebElement element) {
        return ((RemoteWebElement) element).getId();
    }

    protected void screenshot(String name) throws Exception {
        Path output = Path.of("artifacts", "screenshots", target + "-" + name + ".png");
        Files.createDirectories(output.getParent());
        String context = driver.getContext();
        try {
            // Native capture includes Chrome's toolbar and avoids web renderer screenshot issues.
            if (appMode().equals("chrome")) driver.context("NATIVE_APP");
            Files.write(output, driver.getScreenshotAs(OutputType.BYTES));
        } finally {
            if (appMode().equals("chrome")) driver.context(context);
        }
        System.out.println("Screenshot: " + output);
    }

    @AfterMethod(alwaysRun = true)
    public void stopDriver(ITestResult result) throws Exception {
        if (driver != null) {
            try {
                if (!result.isSuccess()) screenshot(result.getMethod().getMethodName() + "-failed");
            } finally {
                driver.quit();
                driver = null;
            }
        }
    }
}
