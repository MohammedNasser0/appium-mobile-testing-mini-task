$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path $PSScriptRoot -Parent
$appsDirectory = Join-Path $projectRoot 'apps'
New-Item -ItemType Directory -Force $appsDirectory | Out-Null
$url = 'https://github.com/appium/android-apidemos/releases/download/v6.0.18/ApiDemos-debug.apk'
Invoke-WebRequest $url -OutFile (Join-Path $appsDirectory 'ApiDemos-debug.apk')
$downloadHash = Get-FileHash (Join-Path $appsDirectory 'ApiDemos-debug.apk') -Algorithm SHA256
if ($downloadHash.Hash -ne 'A9EECF37B26CD084855C530DB81C2BB1B91F4C1B095A04F47AA7C20E2791F686') {
    throw 'The downloaded APK differs from the version used by this project.'
}
$downloadHash
