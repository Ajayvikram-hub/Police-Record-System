@echo off
javac -cp "lib/mysql-connector-j-9.6.0 (1)/mysql-connector-j-9.6.0/mysql-connector-j-9.6.0.jar;." src\*.java
if %errorlevel% neq 0 pause & exit /b %errorlevel%
java -cp "lib/mysql-connector-j-9.6.0 (1)/mysql-connector-j-9.6.0/mysql-connector-j-9.6.0.jar;." src.Main
pause
