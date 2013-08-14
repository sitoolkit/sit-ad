@echo off


set CONF_DIR=%~dp0conf
set DOC_DIR=%~dp0doc

if not exist "%CONF_DIR%" (
	echo デフォルトテンプレートのconfに対するリンクを作成します。
	mklink /d "%CONF_DIR%" "%~dp0src\main\resources\template\conf\sit_javaee6"
)

if not exist "%DOC_DIR%" (
	echo デフォルトテンプレートのdocに対するリンクを作成します。
	mklink /d "%~dp0doc" "%~dp0src\main\resources\template\doc\sit_simple"
)

pause