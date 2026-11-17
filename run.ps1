# Script de lancement du Backend Credit 360
$ErrorActionPreference = "Stop"

Write-Host "========================================================" -ForegroundColor Cyan
Write-Host "   Demarrage du Backend Credit 360 - Banque Populaire   " -ForegroundColor Yellow
Write-Host "========================================================" -ForegroundColor Cyan
Write-Host ""

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

$javaExe = "C:\Program Files\Java\jdk-27\bin\java.exe"
if (-not (Test-Path $javaExe)) {
    $javaExe = "java.exe"
}

Write-Host "Application accessible sur : http://localhost:8080" -ForegroundColor Green
Write-Host "Swagger UI : http://localhost:8080/swagger-ui.html" -ForegroundColor Yellow
Write-Host "Console H2  : http://localhost:8080/h2-console" -ForegroundColor Yellow
Write-Host ""

& $javaExe "@jvm-args.txt"