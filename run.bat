@echo off
setlocal
echo ========================================================
echo   Demarrage du Backend Credit 360 - Banque Populaire
echo ========================================================
echo.
cd /d "%~dp0"

set "JAVA_HOME=C:\Program Files\Java\jdk-27"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo Application accessible sur : http://localhost:8080
echo Swagger UI : http://localhost:8080/swagger-ui.html
echo Console H2  : http://localhost:8080/h2-console
echo.

java.exe @jvm-args.txt

if errorlevel 1 pause

