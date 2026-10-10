param(
    [Parameter(Mandatory=$true)][string]$Udid,
    [ValidateSet('emulator','real')][string]$Target = 'emulator',
    [string]$TestClass = '*',
    [string]$Chromedriver = ''
)
$ErrorActionPreference = 'Stop'
. "$PSScriptRoot\session-env.ps1"
$projectRoot = Split-Path $PSScriptRoot -Parent
Push-Location $projectRoot
try {
    $arguments = @('test', '--tests', $TestClass, "-Dmobile.udid=$Udid", "-Dmobile.target=$Target")
    if ($Chromedriver) { $arguments += "-Dmobile.chromedriver=$Chromedriver" }
    & .\gradlew.bat @arguments
    if ($LASTEXITCODE -ne 0) { throw 'Tests failed. Check build/reports/tests/test/index.html.' }
} finally { Pop-Location }
