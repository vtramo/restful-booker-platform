#!/bin/sh -e

mvn clean

mvn -DskipTests install

/bin/bash ./run_locally.sh -e false
