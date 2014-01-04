@echo off

call "%~dp000_setbuildenv"

set PATH=%PATH%;%ProgramFiles(x86)%\GNU\GnuPG

cd /D %~dp0..

call "%MVN_CMD%" -P release -Dmaven.test.skip=true clean deploy

pause
