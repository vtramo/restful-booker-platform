#!/bin/bash

set -eoux pipefail

GIT_SHORT_COMMIT="${GIT_SHORT_COMMIT}"
DOCKER_REGISTRY_URL="${DOCKER_REGISTRY_URL}"

docker_images=$(docker images --filter "reference=${DOCKER_REGISTRY_URL}/*:${GIT_SHORT_COMMIT}" | awk '{printf "%s:%s\n", $1, $2}' | tail -n +2)
for docker_image in $docker_images; do
  echo "FROM ${docker_image}" | docker build --label e2e_tests_passed="true" -t "${docker_image}" -
  docker push "$docker_image"
done
