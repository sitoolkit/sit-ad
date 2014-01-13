@echo off

set CG_FILE=%~dp0.cg

if exist "%CG_FILE%" (
	del "%CG_FILE%"
) else (
	echo cg file > "%CG_FILE%"
)