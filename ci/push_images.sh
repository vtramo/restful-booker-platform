#!/bin/bash

set -e -u -o pipefail
source .env

printf "Pushing images to %s (in the following order): \n%s\n%s\n%s\n%s\n%s\n%s\n%s\n\n" \
  "${DOCKER_REGISTRY_URL}" \
  "${DOCKER_TAG_AUTH_SERVICE}" \
  "${DOCKER_TAG_BOOKING_SERVICE}" \
  "${DOCKER_TAG_BRANDING_SERVICE}" \
  "${DOCKER_TAG_MESSAGE_SERVICE}" \
  "${DOCKER_TAG_PROXY_SERVICE}" \
  "${DOCKER_TAG_ROOM_SERVICE}" \
  "${DOCKER_TAG_ASSETS_SERVICE}"

echo "Pushing auth service docker image ${DOCKER_TAG_AUTH_SERVICE}..."
docker tag "${DOCKER_TAG_AUTH_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_AUTH_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_AUTH_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_AUTH_SERVICE}"

echo "Pushing booking service docker image ${DOCKER_TAG_BOOKING_SERVICE}..."
docker tag "${DOCKER_TAG_BOOKING_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_BOOKING_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_BOOKING_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_BOOKING_SERVICE}"

echo "Pushing branding service docker image ${DOCKER_TAG_BRANDING_SERVICE}..."
docker tag "${DOCKER_TAG_BRANDING_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_BRANDING_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_BRANDING_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_BRANDING_SERVICE}"

echo "Pushing message service docker image ${DOCKER_TAG_MESSAGE_SERVICE}..."
docker tag "${DOCKER_TAG_MESSAGE_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_MESSAGE_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_MESSAGE_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_MESSAGE_SERVICE}"

echo "Pushing proxy service docker image ${DOCKER_TAG_PROXY_SERVICE}..."
docker tag "${DOCKER_TAG_PROXY_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_PROXY_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_PROXY_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_PROXY_SERVICE}"

echo "Pushing room service docker image ${DOCKER_TAG_ROOM_SERVICE}..."
docker tag "${DOCKER_TAG_ROOM_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_ROOM_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_ROOM_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_ROOM_SERVICE}"

echo "Pushing report service docker image ${DOCKER_TAG_REPORT_SERVICE}..."
docker tag "${DOCKER_TAG_REPORT_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_REPORT_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_REPORT_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_REPORT_SERVICE}"

echo "Pushing assets service docker image ${DOCKER_TAG_ASSETS_SERVICE}..."
docker tag "${DOCKER_TAG_ASSETS_SERVICE}" "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_ASSETS_SERVICE}"
docker push --quiet "${DOCKER_REGISTRY_URL}/${DOCKER_TAG_ASSETS_SERVICE}"
printf "Docker image %s pushed.\n\n" "${DOCKER_TAG_ASSETS_SERVICE}"