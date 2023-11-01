#!/bin/bash

set -e -u -o pipefail
source .env

printf "Beginning image building (in the following order): \n%s\n%s\n%s\n%s\n%s\n%s\n%s\n\n" \
  "${DOCKER_TAG_AUTH_SERVICE}" \
  "${DOCKER_TAG_BOOKING_SERVICE}" \
  "${DOCKER_TAG_BRANDING_SERVICE}" \
  "${DOCKER_TAG_MESSAGE_SERVICE}" \
  "${DOCKER_TAG_PROXY_SERVICE}" \
  "${DOCKER_TAG_ROOM_SERVICE}" \
  "${DOCKER_TAG_ASSETS_SERVICE}"


echo "Building auth service docker image ${DOCKER_TAG_AUTH_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_AUTH_SERVICE}" ../auth
printf "Docker image %s built.\n\n" "${DOCKER_TAG_AUTH_SERVICE}"

echo "Building booking service docker image ${DOCKER_TAG_BOOKING_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_BOOKING_SERVICE}" ../booking
printf "Docker image %s built.\n\n" "${DOCKER_TAG_BOOKING_SERVICE}"

echo "Building branding service docker image ${DOCKER_TAG_BRANDING_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_BRANDING_SERVICE}" ../branding
printf "Docker image %s built.\n\n" "${DOCKER_TAG_BRANDING_SERVICE}"

echo "Building message service docker image ${DOCKER_TAG_MESSAGE_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_MESSAGE_SERVICE}" ../message
printf "Docker image %s built.\n\n" "${DOCKER_TAG_MESSAGE_SERVICE}"

echo "Building proxy service docker image ${DOCKER_TAG_PROXY_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_PROXY_SERVICE}" ../proxy
printf "Docker image %s built.\n\n" "${DOCKER_TAG_PROXY_SERVICE}"

echo "Building room service docker image ${DOCKER_TAG_ROOM_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_ROOM_SERVICE}" ../room
printf "Docker image %s built.\n\n" "${DOCKER_TAG_ROOM_SERVICE}"

echo "Building report service docker image ${DOCKER_TAG_REPORT_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_REPORT_SERVICE}" ../report
printf "Docker image %s built.\n\n" "${DOCKER_TAG_REPORT_SERVICE}"

echo "Building assets service docker image ${DOCKER_TAG_ASSETS_SERVICE}..."
docker build --quiet -t "${DOCKER_TAG_ASSETS_SERVICE}" ../assets
printf "Docker image %s built.\n\n" "${DOCKER_TAG_ASSETS_SERVICE}"