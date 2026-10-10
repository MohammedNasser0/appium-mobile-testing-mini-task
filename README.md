# Appium Mobile Testing

A small Java project for practicing Android tests with Appium.

It tests Android Settings, the API Demos APK, four gestures, and a form in Chrome. TestNG is used for assertions, and `BaseTest` handles the driver setup.

Seven emulator tests and five physical phone tests passed. See [run evidence](docs/RESULTS.md) for reports and screenshots.

## Setup on Windows

1. Install Node.js 24 and Android Studio. In Android Studio, install the Android SDK, Platform Tools, Command-line Tools and an emulator image. Create an AVD in Device Manager.
2. Use Java 17 or newer. I used the Java 21 runtime included with Android Studio.
3. Open PowerShell in this folder and run:

```powershell
. .\scripts\session-env.ps1
npm.cmd ci
.\scripts\download-apk.ps1
npm.cmd run doctor
.\gradlew.bat testClasses
```

Edit the Java/SDK paths in `session-env.ps1` if your installation is different. The script sets `JAVA_HOME`, `ANDROID_HOME` and adds Java, platform-tools, emulator and command-line tools to PATH for the current terminal. Run it in each new terminal.

`npm run doctor` uses `appium driver doctor uiautomator2`, the current replacement for the old appium-doctor package. Required checks should report **0 required fixes needed**. Optional tools for app bundles, recording and streaming are separate from the tests here.

## Run

Start the emulator from Android Studio. In one terminal:

```powershell
. .\scripts\session-env.ps1
npm.cmd run server
```

In another terminal, check `adb devices`, then run a test with its device serial:

```powershell
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass mobile.BuiltInSettingsTest
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass mobile.ApkInteractionTest
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass '*GestureTest'
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass mobile.DragAndDropTest
```

The Settings test expects English Android labels. The APK tests use API Demos v6.0.18 from the [official release](https://github.com/appium/android-apidemos/releases/tag/v6.0.18).

For a real phone, enable Developer Options and USB Debugging, connect the USB cable, and accept the debugging prompt on the phone. Replace `PHONE_SERIAL` below with the serial from `adb devices`:

```powershell
.\scripts\run-tests.ps1 -Udid PHONE_SERIAL -Target real -TestClass mobile.RealDeviceTest
.\scripts\run-tests.ps1 -Udid PHONE_SERIAL -Target real -TestClass '*GestureTest'
.\scripts\run-tests.ps1 -Udid PHONE_SERIAL -Target real -TestClass mobile.DragAndDropTest
```

The setup checks that `-Target real` actually points to a physical device. `RealDeviceTest` is excluded from emulator runs.

Phone APK tests use `noReset` and `forceAppLaunch` because the OPPO phone blocks Appium's data-clear command. Each test starts from the app homepage. The slide test first moves the thumb to a known position, then checks that dragging increases its value.

### Chrome

Install Chrome on the Android device. This script reads its version and downloads a matching Windows Chromedriver from [Chrome for Testing](https://googlechromelabs.github.io/chrome-for-testing/):

```powershell
.\scripts\download-chromedriver.ps1 -Udid emulator-5554
adb -s emulator-5554 reverse tcp:8777 tcp:8777
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass mobile.ChromeWebTest -Chromedriver '.\tools\chromedriver-win64\chromedriver.exe'
```

The test serves a small local page at `http://127.0.0.1:8777/`, enters a name and checks the greeting. Use the phone serial and `-Target real` for a phone run. Remove forwarding afterwards with `adb -s SERIAL reverse --remove tcp:8777`.

To run the whole emulator suite, pass `-TestClass '*'` and the same `-Chromedriver` argument. Seven tests run; the physical device class is excluded.

## Inspector and results

Open `http://127.0.0.1:4723/inspector` while the server is running. See [locators](docs/LOCATORS.md) for capabilities and element examples.

- Java tests: `src/test/java/mobile`
- Test report: `build/reports/tests/test/index.html`
- Screenshots: `artifacts/screenshots`
- [Run evidence](docs/RESULTS.md)
- [Java cheat sheet](docs/APPIUM_JAVA_CHEAT_SHEET.md)

Appium, the Android driver and Inspector are pinned in `package-lock.json`. Java libraries are listed in `build.gradle`.

If Chrome's page is blank in emulator screenshots, close the emulator and try a software renderer:

```powershell
emulator -avd Medium_Phone -gpu swiftshader -feature -Vulkan -no-snapshot-load
```

Replace `Medium_Phone` with your AVD name from `emulator -list-avds`. This was needed for the emulator used here.
