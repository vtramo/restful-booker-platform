@echo off

echo:
echo ####### RESTFUL-BOOKER-PLATFORM #######
echo ####                               ####
echo ####    STARTING APPLICATION...    ####
echo ####                               ####
echo #######################################
echo:

docker compose -f ./docker-compose.yml up -d

call node .utilities/local_monitor.js

echo:
echo ####### RESTFUL-BOOKER-PLATFORM #######
echo ####                               ####
echo ####      APPLICATION READY        ####
echo ####                               ####
echo ####         Available at:         ####
echo ####     http://localhost:8080     ####
echo ####                               ####
echo ####      PRESS ENTER TO QUIT      ####
echo ####                               ####
echo #######################################
echo:
set /p=

echo Exiting Restful-booker-platform....
docker compose -f ./docker-compose.yml down