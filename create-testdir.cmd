@echo off

cd /D %~dp0

mklink /d doc src\main\resources\template\doc\sit_simple
mklink /d conf src\main\resources\template\conf\sit_javaee6

pause