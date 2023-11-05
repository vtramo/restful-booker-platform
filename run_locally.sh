#!/bin/sh

docker_compose_down () {
  docker compose down
}

printf "\n####### RESTFUL-BOOKER-PLATFORM #######
####                               ####
####    STARTING APPLICATION...    ####
####                               ####
#######################################\n\n"

docker compose -f ./docker-compose.yml up -d

node .utilities/local_monitor.js

printf "\n####### RESTFUL-BOOKER-PLATFORM #######
####                               ####
####      APPLICATION READY        ####
####                               ####
####         Available at:         ####
####     http://localhost:8080     ####
####                               ####
#######################################"

trap docker_compose_down INT

sleep 999d