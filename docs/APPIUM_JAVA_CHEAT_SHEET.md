# Appium Java cheat sheet

My notes for the commands used in this task. Examples use Appium 3, Java Client 10 and UiAutomator2.

## Capabilities and session

Use W3C capabilities. Appium-specific names have the `appium:` prefix; `UiAutomator2Options` adds it for you.

```java
UiAutomator2Options options = new UiAutomator2Options()
    .setPlatformName("Android")
    .setDeviceName("Android emulator")
    .setUdid("emulator-5554")
    .setAppPackage("com.android.settings")
    .setAppActivity("com.android.settings.Settings");
AndroidDriver driver = new AndroidDriver(
    URI.create("http://127.0.0.1:4723").toURL(), options);
```

For an APK, give an absolute path. For a phone, change `udid` to the serial reported by `adb devices`.

```java
options.setApp(Path.of("apps/ApiDemos-debug.apk").toAbsolutePath().toString());
```

## Locators

```java
WebElement input = driver.findElement(By.id("io.appium.android.apis:id/edit"));
WebElement views = driver.findElement(AppiumBy.accessibilityId("Views"));
WebElement row = driver.findElement(By.xpath("//*[@text='Network & internet']"));
WebElement list = driver.findElement(AppiumBy.androidUIAutomator(
    "new UiSelector().resourceId(\"android:id/list\")"));
List<WebElement> matches = driver.findElements(By.className("android.widget.Button"));
```

Use Inspector to check the ID or content description. `findElement` throws when missing; `findElements` returns an empty list. XPath is useful when there is no stable ID but is usually slower.

## Waits and element actions

```java
WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
    By.id("io.appium.android.apis:id/edit")));
input.clear();
input.sendKeys("Hello");
driver.hideKeyboard();
driver.findElement(By.id("io.appium.android.apis:id/check1")).click();
String text = input.getText();
boolean visible = input.isDisplayed();
String checked = driver.findElement(By.id("io.appium.android.apis:id/check1"))
    .getAttribute("checked");
Assert.assertEquals(checked, "true");
```

Prefer an explicit wait for the condition you need instead of `Thread.sleep`.

## Driver commands

```java
driver.navigate().back();
driver.activateApp("io.appium.android.apis");
driver.terminateApp("io.appium.android.apis");
driver.installApp(Path.of("apps/ApiDemos-debug.apk").toAbsolutePath().toString());
boolean installed = driver.isAppInstalled("io.appium.android.apis");
String activity = driver.currentActivity();
String source = driver.getPageSource();
Files.write(Path.of("screen.png"), driver.getScreenshotAs(OutputType.BYTES));
driver.quit(); // End the session in teardown, even after a failed assertion.
```

## Tap and long press

UiAutomator2 gestures use `executeScript`. The element ID is Appium's internal element ID, not its Android resource ID.

```java
String id = ((RemoteWebElement) checkbox).getId();
driver.executeScript("mobile: clickGesture", Map.of("elementId", id));
driver.executeScript("mobile: longClickGesture",
    Map.of("elementId", id, "duration", 1000));
```

## Scroll

```java
boolean canScrollMore = Boolean.TRUE.equals(driver.executeScript(
    "mobile: scrollGesture", Map.of(
        "elementId", ((RemoteWebElement) list).getId(),
        "direction", "down", "percent", 0.8)));
```

Repeat a bounded number of times until the target is visible or `canScrollMore` is false. Assert that the intended row appears.

## Swipe / slide

```java
driver.executeScript("mobile: swipeGesture", Map.of(
    "left", 40, "top", 300, "width", 800, "height", 300,
    "direction", "left", "percent", 0.75));
```

For a slider, drag its thumb along the track and assert the displayed progress changes. Coordinates should come from the element bounds, not fixed screen pixels.

```java
Rectangle r = seekBar.getRect();
driver.executeScript("mobile: dragGesture", Map.of(
    "startX", r.x + 12, "startY", r.y + r.height / 2,
    "endX", r.x + (int)(r.width * 0.8), "endY", r.y + r.height / 2,
    "speed", 600));
```

## Drag and drop

```java
Rectangle r = destination.getRect();
driver.executeScript("mobile: dragGesture", Map.of(
    "elementId", ((RemoteWebElement) source).getId(),
    "endX", r.x + r.width / 2, "endY", r.y + r.height / 2,
    "speed", 500));
Assert.assertEquals(result.getText(), "Dropped!");
```

## Chrome on Android

Use a Chromedriver version matching Chrome on the Android device. `browserName` is a standard W3C capability.

```java
UiAutomator2Options options = new UiAutomator2Options()
    .setPlatformName("Android").setDeviceName("Android")
    .setUdid("emulator-5554");
options.withBrowserName("chrome");
options.setChromedriverExecutable("C:\\tools\\chromedriver.exe");
AndroidDriver driver = new AndroidDriver(
    URI.create("http://127.0.0.1:4723").toURL(), options);
driver.get("http://127.0.0.1:8777/");
driver.findElement(By.id("name")).sendKeys("Mohammed");
driver.findElement(By.id("submit")).click();
Assert.assertEquals(driver.findElement(By.id("result")).getText(), "Hello, Mohammed!");
```

The local fixture needs `adb -s SERIAL reverse tcp:8777 tcp:8777` first. In web context, use HTML IDs/CSS selectors rather than Android resource IDs.

## Hybrid app contexts

```java
Set<String> contexts = driver.getContextHandles();
driver.context("WEBVIEW_io.appium.android.apis"); // Use an actual returned context name.
driver.context("NATIVE_APP");
```

## References

- [Appium Java Client and Selenium compatibility](https://github.com/appium/java-client)
- [UiAutomator2 setup and capabilities](https://github.com/appium/appium-uiautomator2-driver)
- [Android gesture arguments](https://github.com/appium/appium-uiautomator2-driver/blob/master/docs/android-mobile-gestures.md)
- [Appium Inspector](https://appium.github.io/appium-inspector/latest/quickstart/installation/)
