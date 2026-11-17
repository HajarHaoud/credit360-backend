# Script de compilation du Backend Credit 360
$ErrorActionPreference = "Stop"

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "   Compilation du Backend Credit 360 - Banque Populaire " -ForegroundColor Yellow
Write-Host "========================================================" -ForegroundColor Cyan

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

$m2Repo = "$env:USERPROFILE\.m2\repository"
$jars = Get-ChildItem -Path $m2Repo -Recurse -Filter "*.jar" | Where-Object { $_.FullName -notmatch "sources" -and $_.FullName -notmatch "javadoc" -and $_.FullName -notmatch "1\.7\.36" -and $_.FullName -notmatch "slf4j-simple" } | ForEach-Object { $_.FullName }
$cp = "$scriptDir\target\classes;" + ($jars -join ";")

if (-not (Test-Path "$scriptDir\target\classes")) {
    New-Item -ItemType Directory -Path "$scriptDir\target\classes" -Force | Out-Null
}

$javacArgs = @(
    "-parameters",
    "--release", "17",
    "-encoding", "UTF-8",
    "-cp", $cp,
    "-d", "$scriptDir\target\classes"
)
[System.IO.File]::WriteAllLines("$scriptDir\javac-options.txt", $javacArgs)

$sources = Get-ChildItem -Path "$scriptDir\src\main\java" -Recurse -Filter "*.java" | ForEach-Object { $_.FullName }
[System.IO.File]::WriteAllLines("$scriptDir\sources.txt", $sources)

$javacExe = "C:\Program Files\Java\jdk-27\bin\javac.exe"
if (-not (Test-Path $javacExe)) {
    $javacExe = "javac.exe"
}

Write-Host "Compilation de $($sources.Count) fichiers Java avec -parameters..." -ForegroundColor Cyan
& $javacExe "@$scriptDir\javac-options.txt" "@$scriptDir\sources.txt"

Remove-Item "$scriptDir\javac-options.txt" -Force -ErrorAction SilentlyContinue
Remove-Item "$scriptDir\sources.txt" -Force -ErrorAction SilentlyContinue

Copy-Item -Path "$scriptDir\src\main\resources\*" -Destination "$scriptDir\target\classes" -Recurse -Force

Write-Host "[SUCCES] Compilation terminee avec succes dans target\classes !" -ForegroundColor Green
