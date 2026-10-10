# Test run evidence

Run date: 10 October 2026. Screenshots are from actual Appium sessions. Failed-run screenshots are kept locally with a `-failed` suffix and are excluded from the submission.

The environment uses Windows, Java 21.0.10, Node.js 24.17.0, Appium 3.8.0, UiAutomator2 8.7.0, Java Client 10.1.1 and Selenium 4.44.0. The AVD is `Medium_Phone`, Android 17 / API 37. Chrome and Chromedriver are version 154.0.8037.92.

## Environment check

Doctor reported **0 required fixes needed, 3 optional fixes possible**. The optional warnings are for bundletool (app bundles), ffmpeg (recording) and GStreamer (streaming). These features are not used by the tests.

- [Doctor output with local paths redacted](../artifacts/logs/doctor.txt)
- [Doctor screenshot](screenshots/doctor-output.jpg)

The Doctor image is a browser view of the saved diagnostic output with local user/project paths redacted. It is not a screenshot of a native terminal; the diagnostic results and warning lines are unchanged. Unredacted originals remain local in the ignored `artifacts/raw` folder.

## Emulator evidence

| Test | Verified result | Screenshot |
| --- | --- | --- |
| Built-in Settings | Network & internet opens and Internet is visible | [Settings](../artifacts/screenshots/emulator-built-in-settings.png) |
| APK interaction | Input text matches and checkbox is checked | [APK](../artifacts/screenshots/emulator-apk-interaction.png) |
| Tap | Checkbox changes from unchecked to checked | [Before](../artifacts/screenshots/emulator-tap-before.png), [after](../artifacts/screenshots/emulator-tap-after.png) |
| Scroll | WebView row starts offscreen and becomes visible | [Before](../artifacts/screenshots/emulator-scroll-before.png), [after](../artifacts/screenshots/emulator-scroll-after.png) |
| Slide | Seek Bar value increases from zero | [Before](../artifacts/screenshots/emulator-slide-before.png), [after](../artifacts/screenshots/emulator-slide-after.png) |
| Drag & Drop | Result label becomes Dropped! | [Before](../artifacts/screenshots/emulator-drag-before.png), [after](../artifacts/screenshots/emulator-drag-after.png) |
| Chrome | Form input produces Hello, Mohammed! | [Chrome](../artifacts/screenshots/emulator-chrome-web.png) |

Final combined run: **7 tests passed, 0 failed, 0 skipped**. Build completed successfully in 2 minutes 38 seconds.

- [Saved Gradle HTML report](../artifacts/test-report/index.html)
- [Original final console output](../artifacts/logs/emulator-tests.txt)
- [Report screenshot](screenshots/emulator-test-summary.jpg)

Chrome screenshots needed the emulator's SwiftShader renderer with Vulkan disabled. Settings uses `noReset` plus `forceAppLaunch` to restart at the homepage without clearing Settings data.

## Inspector

Inspector was connected to the emulator to identify and check the locators:

- [Settings text/XPath and resource ID](screenshots/inspector-settings-element.jpg)
- [APK checkbox resource ID, accessibility ID and XPath](screenshots/inspector-apk-id.jpg)
- [Locator notes and capabilities](LOCATORS.md)

## Physical Android device — pending

No physical Android device was available in `adb devices` during these runs. `RealDeviceTest` is implemented, but its execution and physical-device screenshots are still pending. The four gesture tests have been executed on the emulator, not verified on a physical phone yet.

Connect a phone, approve USB Debugging on the phone, then follow the real-device commands in the README. Emulator screenshots are not presented as physical-device evidence.
