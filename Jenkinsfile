pipeline {
    agent {
        label 'build-agent'
    }

    environment {
        DOCKER_REGISTRY_URL = 'localhost:5000'
    }

    options {
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
                    env.GIT_SHORT_COMMIT = "${GIT_COMMIT[0..7]}"
                    env.GIT_PREVIOUS_SUCCESSFUL_SHORT_COMMIT = "${GIT_PREVIOUS_SUCCESSFUL_COMMIT[0..7]}"
                    env.GIT_COMMITTER_NAME = sh (script: "git show -s --format='%an' ${GIT_COMMIT}", returnStdout: true)
                    env.GIT_COMMITTER_EMAIL = sh (script: "git show -s --format='%ae' ${GIT_COMMIT}", returnStdout: true)
                    env.GIT_COMMIT_MSG = sh (script: "git log --format=%B -n 1 ${GIT_COMMIT}", returnStdout: true)
                }
            }
        }

        stage('Services Pipeline') {
            parallel {
                stage('Mine Repository') {
                    steps {
                        mineRepository()
                    }
                }

                stage('Auth Service') {
                    when {
                        anyOf {
                            changeset "auth/Dockerfile"
                            changeset "auth/src/main/java/**/*.java"
                            changeset "auth/src/test/java/**/*.java"
                        }
                    }

                    environment {
                        RBP_AUTH_SERVICE_MAIN_DIR = 'auth'
                        RBP_AUTH_SERVICE_CI_DIR = 'auth/ci'
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

                        stage('[auth] SonarQube Scan') {
                            options {
                                timeout(time: 1, unit: 'MINUTES')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                    withSonarQubeEnv(installationName: 'sonarqube') {
                                        sh '''
                                            mvn sonar:sonar \
                                                -Dsonar.projectKey=restful-booker-platform-auth \
                                                -Dsonar.projectName=restful-booker-platform-auth \
                                        '''
                                    }
                                }
                            }
                        }

                        stage('[auth] Quality Gates') {
                            steps {
                                timeout(time: 1, unit: 'MINUTES') {
                                    waitForQualityGate abortPipeline: true
                                }
                            }
                        }

                        stage('[auth] Build Image') {
                            options {
                                timeout(time: 30, unit: 'SECONDS')
                            }

                            steps {
                                dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                    sh '''
                                        docker build \
                                            --build-arg BUILD_NUMBER=${BUILD_NUMBER} \
                                            --build-arg BUILD_TAG=${BUILD_TAG} \
                                            --build-arg GIT_COMMIT=${GIT_COMMIT} \
                                            -t ${DOCKER_REGISTRY_URL}/rbp-auth:${GIT_SHORT_COMMIT} .
                                    '''
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
                                RBP_AUTH_SERVICE_DOCKER_IMAGE_TAG = "${GIT_SHORT_COMMIT}"
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
                                sh 'docker push ${DOCKER_REGISTRY_URL}/rbp-auth:${GIT_SHORT_COMMIT}'
                            }
                        }
                    }

                    post {
                        always {
                            dir("${RBP_AUTH_SERVICE_MAIN_DIR}") {
                                junit(
                                    testResults: 'target/surefire-reports/**/*.xml,target/failsafe-reports/**/*.xml',
                                    allowEmptyResults: true
                                )
                                jacoco(
                                    execPattern: 'target/**/*.exec',
                                    classPattern: 'target/classes/com/rbp',
                                    sourcePattern: 'src/main/java/com/rbp'
                                )
                                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                                recordIssues(
                                    enabledForFailure: true, aggregatingResults: true,
                                    tools: [
                                        java(),
                                        junitParser(name: 'Unit Test Warnings',
                                                    pattern: 'target/surefire-reports/**/*.xml'),
                                        junitParser(name: 'Integration Test Warnings',
                                                    pattern: 'target/failsafe-reports/**/*.xml')
                                    ]
                                )
                            }

                            dir("${RBP_AUTH_SERVICE_CI_DIR}") {
                                sh '''
                                    docker compose -f docker-compose-test.yaml logs && \
                                    docker compose -f docker-compose-test.yaml down --volumes
                                '''
                            }
                        }

                        success {
                            slackSend(
                                channel: "ci",
                                color: 'good',
                                message: """
                                    :white_check_mark: [auth] Build was successful!
                                    *Branch:* ${GIT_BRANCH}
                                    *Commit ID:* ${GIT_COMMIT}
                                    *Short commit ID:* ${GIT_SHORT_COMMIT}
                                    *Commit message:* ${GIT_COMMIT_MSG}
                                    *Previous successful commit ID:* ${GIT_PREVIOUS_SUCCESSFUL_SHORT_COMMIT} 
                                    *Committer name:* ${GIT_COMMITTER_NAME}
                                    *Committer email:* ${GIT_COMMITTER_EMAIL}
                                    *Build label:* ${BUILD_TAG}
                                    *Build ID:* ${BUILD_ID}
                                    *Build URL:* ${BUILD_URL}
                                """
                            )
                        }

                        failure {
                            slackSend(
                                channel: "ci",
                                color: 'danger',
                                message: """
                                    :x: [auth] Build failed!
                                    *Branch:* ${GIT_BRANCH}
                                    *Commit ID:* ${GIT_COMMIT}
                                    *Short commit ID:* ${GIT_SHORT_COMMIT}
                                    *Commit message:* ${GIT_COMMIT_MSG}
                                    *Previous successful commit ID:* ${GIT_PREVIOUS_SUCCESSFUL_SHORT_COMMIT} 
                                    *Committer name:* ${GIT_COMMITTER_NAME}
                                    *Committer email:* ${GIT_COMMITTER_EMAIL}
                                    *Build label:* ${BUILD_TAG}
                                    *Build ID:* ${BUILD_ID}
                                    *Build URL:* ${BUILD_URL}
                                """
                            )
                        }
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

                    environment {
                        RBP_ROOM_SERVICE_MAIN_DIR = 'room'
                        RBP_ROOM_SERVICE_CI_DIR = 'room/ci'
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
                    when {
                        anyOf {
                            changeset "booking/Dockerfile"
                            changeset "booking/src/main/java/**/*.java"
                            changeset "booking/src/test/java/**/*.java"
                        }
                    }

                    environment {
                        RBP_BOOKING_SERVICE_MAIN_DIR = 'booking'
                        RBP_BOOKING_SERVICE_CI_DIR = 'booking/ci'
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
                        
                        stage('[booking] Unit Tests') {
                            options {
                                timeout(time: 20, unit: 'SECONDS')
                            }

                            steps {
                                dir("${RBP_BOOKING_SERVICE_MAIN_DIR}") {
                                    sh 'mvn test'
                                }
                            }
                        }
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

                    environment {
                        RBP_BRANDING_SERVICE_MAIN_DIR = 'branding'
                        RBP_BRANDING_SERVICE_CI_DIR = 'branding/ci'
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
                    when {
                        anyOf {
                            changeset "message/Dockerfile"
                            changeset "message/src/main/java/**/*.java"
                            changeset "message/src/test/java/**/*.java"
                        }
                    }

                    environment {
                        RBP_MESSAGE_SERVICE_MAIN_DIR = 'message'
                        RBP_MESSAGE_SERVICE_CI_DIR = 'message/ci'
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
                    when {
                        anyOf {
                            changeset "report/Dockerfile"
                            changeset "report/src/main/java/**/*.java"
                            changeset "report/src/test/java/**/*.java"
                        }
                    }

                    environment {
                        RBP_REPORT_SERVICE_MAIN_DIR = 'report'
                        RBP_REPORT_SERVICE_CI_DIR = 'report/ci'
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
                RBP_TEST_PILOT_MAIN_DIR = 'test-pilot'
            }

            options {
                timeout(time: 3, unit: 'MINUTES')
            }

            steps {
                dir("${RBP_TEST_PILOT_MAIN_DIR}") {
                     sh 'ls'
                     sh 'printenv'
                }
            }
        }
    }
}