param([Parameter(Mandatory=$true)][string]$Udid)
$ErrorActionPreference = 'Stop'
. "$PSScriptRoot\session-env.ps1"
$projectRoot = Split-Path $PSScriptRoot -Parent
$packageInfo = & adb -s $Udid shell dumpsys package com.android.chrome
if ($LASTEXITCODE -ne 0) { throw 'Cannot read Chrome version from the selected device.' }
$versionLine = $packageInfo | Select-String 'versionName=' | Select-Object -First 1
if (!$versionLine -or $versionLine.Line -notmatch 'versionName=(\d+)\.') {
    throw 'Chrome is not installed on the selected Android device.'
}
$milestone = $Matches[1]
$manifest = Invoke-RestMethod 'https://googlechromelabs.github.io/chrome-for-testing/latest-versions-per-milestone-with-downloads.json'
$driverInfo = $manifest.milestones.$milestone
if (!$driverInfo) { throw "No official Chromedriver download found for milestone $milestone." }
$download = $driverInfo.downloads.chromedriver | Where-Object platform -eq 'win64'
if (!$download) { throw 'No Windows 64-bit driver is available for this Chrome version.' }
$toolsDirectory = Join-Path $projectRoot 'tools'
New-Item -ItemType Directory -Force $toolsDirectory | Out-Null
Invoke-WebRequest $download.url -OutFile (Join-Path $toolsDirectory 'chromedriver.zip')
Expand-Archive (Join-Path $toolsDirectory 'chromedriver.zip') -DestinationPath $toolsDirectory -Force
Write-Host "Downloaded official Chromedriver $($driverInfo.version) for Chrome milestone $milestone"
Write-Host (Join-Path $toolsDirectory 'chromedriver-win64\chromedriver.exe')
