# Restful Booker Test Pilot

Test-Pilot is a dedicated module for running comprehensive tests on the Restful Booker Platform. This module is designed
exclusively for executing sophisticated tests.

## Running E2E Tests (Cucumber)
Before running the e2e tests, make sure you have already built the project with the `build_locally` script.

```mvn clean test -Dcucumber.features=src/test/resources```