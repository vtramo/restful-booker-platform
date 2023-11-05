@echo off

set cmdFileDirectory=%~dp0

cd %cmdFileDirectory%
call mvn clean -DskipTests install
CALL run_locally.cmd false
