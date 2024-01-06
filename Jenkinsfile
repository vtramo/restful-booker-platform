pipeline {
    agent {
        label 'build-agent'
    }

    environment {
        DOCKER_REGISTRY_URL = 'localhost:5000'
    }

    options {
        timestamps()
        skipStagesAfterUnstable()
        parallelsAlwaysFailFast()
        timeout(time: 7, unit: 'MINUTES')
    }

    stages {
        stage('Services Pipeline') {
            parallel {
                stage('Auth Service') {
                    environment {
                        RBP_AUTH_SERVICE_MAIN_DIR = 'restful-booker-platform/auth'
                        RBP_AUTH_SERVICE_CI_DIR = 'restful-booker-platform/auth/ci'
                    }

                    stages {
                        stage('[auth] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }

                        stage('[auth] Unit Tests') {
                            options {
                                timeout(time: 20, unit: 'SECONDS')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                    sh 'mvn test'
                                }
                            }
                        }

                        stage('[auth] Integration Tests') {
                            options {
                                timeout(time: 40, unit: 'SECONDS')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                    sh 'mvn verify -Dskip.surefire.tests=true'
                                }
                            }
                        }

                        stage('[auth] Build Image') {
                            options {
                                timeout(time: 30, unit: 'SECONDS')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                    sh 'docker build -t ${DOCKER_REGISTRY_URL}/rbp-auth:1.0 .'
                                }
                            }
                        }

                        stage('[auth] Performance Tests') {
                            options {
                                timeout(time: 1, unit: 'MINUTES')
                            }

                            environment {
                                RBP_AUTH_SERVICE_HOSTNAME = 'rbp-auth'
                                RBP_AUTH_SERVICE_PORT = '3004'
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_CI_DIR}") {
                                    sh 'docker compose -f docker-compose-test.yaml up -d --build --wait'
                                    bzt """-o settings.env.JMETER_HOME=${JMETER_HOME} \
                                        -o settings.env.RBP_AUTH_SERVICE_HOSTNAME=${RBP_AUTH_SERVICE_HOSTNAME} \
                                        -o settings.env.RBP_AUTH_SERVICE_PORT=${RBP_AUTH_SERVICE_PORT} \
                                        performance-test.yaml"""
                                }
                            }
                        }

                        stage('[auth] Push Image') {
                            when {
                              expression {
                                currentBuild.result == null || currentBuild.result == 'SUCCESS'
                              }
                            }

                            options {
                                timeout(time: 30, unit: 'SECONDS')
                            }

                            steps {
                                sh 'docker push ${DOCKER_REGISTRY_URL}/rbp-auth:1.0'
                            }
                        }
                    }

                    post {
                        always  {
                            dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                junit 'target/surefire-reports/**/*.xml'
                                jacoco(
                                    execPattern: 'target/**/*.exec',
                                    classPattern: 'target/classes/com/rbp',
                                    sourcePattern: 'src/main/java/com/rbp'
                                )
                                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                            }

                            dir("${RBP_AUTH_SERVICE_CI_DIR}") {
                                sh '''
                                    docker compose -f docker-compose-test.yaml logs && \
                                    docker compose -f docker-compose-test.yaml down --volumes
                                '''
                            }
                        }
                    }
                }

                stage('Room Service') {
                    environment {
                        RBP_ROOM_SERVICE_MAIN_DIR = 'restful-booker-platform/room'
                        RBP_ROOM_SERVICE_CI_DIR = 'restful-booker-platform/room/ci'
                    }

                    stages {
                        stage('[room] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_ROOM_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }
                    }
                }

                stage('Booking Service') {
                    environment {
                        RBP_BOOKING_SERVICE_MAIN_DIR = 'restful-booker-platform/room'
                        RBP_BOOKING_SERVICE_CI_DIR = 'restful-booker-platform/room/ci'
                    }

                    stages {
                        stage('[booking] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_BOOKING_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }
                    }
                }

                stage('Branding Service') {
                    environment {
                        RBP_BRANDING_SERVICE_MAIN_DIR = 'restful-booker-platform/branding'
                        RBP_BRANDING_SERVICE_CI_DIR = 'restful-booker-platform/branding/ci'
                    }

                    stages {
                        stage('[branding] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_BRANDING_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }
                    }
                }

                stage('Message Service') {
                    environment {
                        RBP_MESSAGE_SERVICE_MAIN_DIR = 'restful-booker-platform/branding'
                        RBP_MESSAGE_SERVICE_CI_DIR = 'restful-booker-platform/branding/ci'
                    }

                    stages {
                        stage('[message] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_MESSAGE_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }
                    }
                }

                stage('Report Service') {
                    environment {
                        RBP_REPORT_SERVICE_MAIN_DIR = 'restful-booker-platform/report'
                        RBP_REPORT_SERVICE_CI_DIR = 'restful-booker-platform/report/ci'
                    }

                    stages {
                        stage('[report] Build') {
                            options {
                                timeout(time: 3, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_REPORT_SERVICE_MAIN_DIR}") {
                                     sh 'mvn clean package -DskipTests'
                                }
                            }
                        }
                    }
                }
            }
        }

        stage('E2E Tests') {
            environment {
                RBP_TEST_PILOT_MAIN_DIR = 'restful-booker-platform/test-pilot'
            }

            options {
                timeout(time: 3, unit: 'MINUTES')
            }

            steps {
                dir("${RBP_TEST_PILOT_MAIN_DIR}") {
                     sh 'ls'
                }
            }
        }
    }
}