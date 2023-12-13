pipeline {
    agent {
        label 'java-maven'
    }

    environment {
        DOCKER_REGISTRY_URL = 'localhost:5000'
    }

    options {
        timeout(time: 7, unit: 'MINUTES')
    }

    stages {
        stage('Build') {
            options {
                timeout(time: 1, unit: 'MINUTES')
            }

            steps {
                dir('test-pilot') {
                    sh '''
                        mvn clean package
                    '''
                }
            }
        }

        stage('End to end Tests') {
            options {
                timeout(time: 5, unit: 'MINUTES')
            }

            steps {
                dir('test-pilot') {
                    sh '''
                        mvn test -Dcucumber.features=src/test/resources
                    '''
                }
            }
        }

    }

    post {
        always  {
            dir('test-pilot') {
                junit 'target/surefire-reports/**/*.xml'
            }
        }
    }
}
