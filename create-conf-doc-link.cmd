@echo off


set CONF_DIR=%~dp0conf
set DOC_DIR=%~dp0doc

if not exist "%CONF_DIR%" (
	echo �f�t�H���g�e���v���[�g��conf�ɑ΂��郊���N���쐬���܂��B
	mklink /d "%CONF_DIR%" "%~dp0src\main\resources\template\conf\sit_javaee6"
)

if not exist "%DOC_DIR%" (
	echo �f�t�H���g�e���v���[�g��doc�ɑ΂��郊���N���쐬���܂��B
	mklink /d "%~dp0doc" "%~dp0src\main\resources\template\doc\sit_simple"
)

pause