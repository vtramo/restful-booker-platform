@Library('rbp-jenkins-shared-library') _
pipeline {
    agent {
        label 'build-agent'
    }

    environment {
        DOCKER_REGISTRY_URL = 'localhost:5000'
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '100', daysToKeepStr: '', numToKeepStr: '100')
        timestamps()
        skipStagesAfterUnstable()
        parallelsAlwaysFailFast()
        timeout(time: 7, unit: 'MINUTES')
    }

    stages {
        stage('Get git commit info') {
            steps {
                script {
                    env.GIT_COMMIT = sh (script: 'git rev-parse HEAD', returnStdout: true)
                    env.GIT_SHORT_COMMIT = "${env.GIT_COMMIT[0..7]}"
                    env.GIT_PREVIOUS_SUCCESSFUL_SHORT_COMMIT = env.GIT_PREVIOUS_SUCCESSFUL_COMMIT[0..7]
                    env.GIT_COMMITTER_NAME = sh (script: "git show -s --format='%an' ${env.GIT_COMMIT}", returnStdout: true)
                    env.GIT_COMMITTER_EMAIL = sh (script: "git show -s --format='%ae' ${env.GIT_COMMIT}", returnStdout: true)
                    env.GIT_COMMIT_MSG = sh (script: "git log --format=%B -n 1 ${env.GIT_COMMIT}", returnStdout: true)
                }
            }
        }

        stage('Stash git repository') {
            steps {
                stash includes: '**', name: 'rbp', useDefaultExcludes: false
            }
        }

        stage('Services Pipeline') {
            parallel {
                stage('Auth Service') {
                    when {
                        anyOf {
                            changeset "auth/Dockerfile"
                            changeset "auth/src/main/java/**/*.java"
                            changeset "auth/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'auth',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-auth',
                            rbpServicePort: '3004'
                        )
                    }
                }

                stage('Room Service') {
                    when {
                        anyOf {
                            changeset "room/Dockerfile"
                            changeset "room/src/main/java/**/*.java"
                            changeset "room/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'room',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-room',
                            rbpServicePort: '3001',
                            skipPerformanceTests: true
                        )
                    }
                }

                stage('Booking Service') {
                    when {
                        anyOf {
                            changeset "booking/Dockerfile"
                            changeset "booking/src/main/java/**/*.java"
                            changeset "booking/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'booking',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-booking',
                            rbpServicePort: '3000',
                            skipPerformanceTests: true
                        )
                    }
                }

                stage('Branding Service') {
                    when {
                        anyOf {
                            changeset "branding/Dockerfile"
                            changeset "branding/src/main/java/**/*.java"
                            changeset "branding/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'branding',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-branding',
                            rbpServicePort: '3002',
                            skipPerformanceTests: true
                        )
                    }
                }

                stage('Message Service') {
                    when {
                        anyOf {
                            changeset "message/Dockerfile"
                            changeset "message/src/main/java/**/*.java"
                            changeset "message/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'message',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-message',
                            rbpServicePort: '3006',
                            skipPerformanceTests: true
                        )
                    }
                }

                stage('Report Service') {
                    when {
                        anyOf {
                            changeset "report/Dockerfile"
                            changeset "report/src/main/java/**/*.java"
                            changeset "report/src/test/java/**/*.java"
                        }
                    }

                    steps {
                        rbpServicePipeline(
                            serviceName: 'report',
                            nodeLabel: 'build-agent',
                            rbpServiceHostname: 'rbp-report',
                            rbpServicePort: '3005',
                            skipPerformanceTests: true
                        )
                    }
                }
            }
        }

        stage('E2E Tests') {
            environment {
                RBP_TEST_PILOT_MAIN_DIR = 'test-pilot'
                RBP_TEST_PILOT_DOCKER_DIR = "${RBP_TEST_PILOT_MAIN_DIR}/src/test/resources/docker"
                DISABLE_TESTCONTAINERS = 'true'
                RBP_PROXY_URL = 'http://rbp-proxy:8080'
            }

            options {
                timeout(time: 3, unit: 'MINUTES')
            }

            parallel {
                stage('Chrome') {
                    environment {
                        WEB_DRIVER = 'chrome'
                        RBP_E2E_DOCKER_NETWORK = 'rbp-e2e-chrome'
                        RBP_E2E_DOCKER_PROJECT_NAME = 'rbp-e2e-chrome'
                    }

                    agent {
                        label 'rbp-e2e-chrome'
                    }

                    steps {
                        unstash 'rbp'

                        dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                            sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} up -d"
                        }

                        dir("${RBP_TEST_PILOT_MAIN_DIR}") {
                            sh 'mvn clean test -Dcucumber.features=src/test/resources'
                        }
                    }

                    post {
                        always {
                            dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                                sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} down || :"
                            }
                        }
                    }
                }

                stage('Firefox') {
                    environment {
                        WEB_DRIVER = 'firefox'
                        RBP_E2E_DOCKER_NETWORK = 'rbp-e2e-firefox'
                        RBP_E2E_DOCKER_PROJECT_NAME = 'rbp-e2e-firefox'
                    }

                    agent {
                        label 'rbp-e2e-firefox'
                    }

                    steps {
                        unstash 'rbp'

                        dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                            sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} up -d"
                        }

                        dir("${RBP_TEST_PILOT_MAIN_DIR}") {
                            sh 'mvn clean test -Dcucumber.features=src/test/resources'
                        }
                    }

                    post {
                        always {
                            dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                                sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} down || :"
                            }
                        }
                    }
                }

                stage('Edge') {
                    environment {
                        WEB_DRIVER = 'edge'
                        RBP_E2E_DOCKER_NETWORK = 'rbp-e2e-edge'
                        RBP_E2E_DOCKER_PROJECT_NAME = 'rbp-e2e-edge'
                    }

                    agent {
                        label 'rbp-e2e-edge'
                    }

                    steps {
                        unstash 'rbp'

                        dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                            sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} up -d"
                        }

                        dir("${RBP_TEST_PILOT_MAIN_DIR}") {
                            sh 'mvn clean test -Dcucumber.features=src/test/resources'
                        }
                    }

                    post {
                        always {
                            dir("${RBP_TEST_PILOT_DOCKER_DIR}") {
                                sh "docker compose -f docker-compose-test.yaml -p ${RBP_E2E_DOCKER_PROJECT_NAME} down || :"
                            }
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            rbpSendSlackNotification()
        }
    }
}