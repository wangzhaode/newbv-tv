param([switch]$Test)

$ErrorActionPreference = 'Stop'
$workspace = Split-Path -Parent $PSScriptRoot
$localJdk = Join-Path $workspace 'jdk17.0.21_1'
$localGradle = Join-Path $workspace 'gradle-9.3.1/bin/gradle.bat'

if (Test-Path (Join-Path $localJdk 'bin/java.exe')) {
    $env:JAVA_HOME = $localJdk
}
if (-not $env:ANDROID_HOME) {
    $env:ANDROID_HOME = Join-Path $env:LOCALAPPDATA 'Android/Sdk'
}
if (-not $env:GRADLE_USER_HOME) {
    $env:GRADLE_USER_HOME = Join-Path $workspace 'gradle-home'
}
$env:NEWBV_VERSION_BASE = '1.2.1'

$gradle = if (Test-Path $localGradle) { $localGradle } else { Join-Path $PSScriptRoot 'gradlew.bat' }
$tasks = @(':app:assembleDebug')
if ($Test) {
    $tasks += ':bili-api:test', '--tests', 'dev.frost819.newbv.biliapi.http.ApiDnsTest'
}

Push-Location $PSScriptRoot
try {
    & $gradle --console=plain --max-workers=4 --no-watch-fs @tasks
    if ($LASTEXITCODE -ne 0) { exit $LASTEXITCODE }
    Get-ChildItem 'app/build/outputs/apk/debug' -Filter '*.apk' | Select-Object -ExpandProperty FullName
} finally {
    Pop-Location
}
