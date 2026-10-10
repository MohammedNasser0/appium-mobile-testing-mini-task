# Appium Mobile Testing

A small Java project to practice Android automation using Appium and TestNG.

It includes tests for Android Settings, API Demos, Tap, Scroll, Slide, Drag & Drop, and a simple web form in Chrome.

## Setup

Install Node.js, Android Studio and Java 17 or newer. Set the Java and Android SDK paths in `scripts/session-env.ps1`, then run:

```powershell
. .\scripts\session-env.ps1
npm.cmd ci
.\scripts\download-apk.ps1
npm.cmd run doctor
npm.cmd run server
```

## Run a test

Start an emulator or connect an Android phone with USB Debugging enabled. In another PowerShell terminal:

```powershell
.\scripts\run-tests.ps1 -Udid emulator-5554 -TestClass mobile.ApkInteractionTest
```

For a phone, replace the serial and add `-Target real`.

## Results

7 tests passed on the emulator and 5 on a real phone.

- [Screenshots and reports](docs/RESULTS.md)
- [Full setup and Chrome commands](docs/SETUP.md)
- [Java cheat sheet](docs/APPIUM_JAVA_CHEAT_SHEET.md)
- [Inspector locators](docs/LOCATORS.md)
