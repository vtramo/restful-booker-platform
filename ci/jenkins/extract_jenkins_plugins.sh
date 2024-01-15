#!/bin/bash

set -eoux pipefail

JENKINS_URL=${JENKINS_URL}
JENKINS_USER=${JENKINS_USER}
JENKINS_PASSWORD=${JENKINS_PASSWORD}

curl "${JENKINS_URL}/jnlpJars/jenkins-cli.jar" > jenkins-cli.jar
java -jar jenkins-cli.jar -s ${JENKINS_URL} -auth ${JENKINS_USER}:${JENKINS_PASSWORD} groovy = < plugins.groovy > plugins.txt
rm -rf jenkins-cli.jar