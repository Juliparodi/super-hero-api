pipeline {
    agent any
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    environment {
        CODECOV_TOKEN = credentials('CodecovToken')
    }

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                checkout([$class: 'GitSCM', userRemoteConfigs: [[url: 'https://github.com/Juliparodi/super-hero-api.git']]])
            }
        }
        stage('Build') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/Juliparodi/super-hero-api.git'

                // Run Maven on a Unix agent.
                sh "mvn package -DskipTests"
            }
        }
        stage('Test') {
            steps {
                // Get some code from a GitHub repository
                git 'https://github.com/Juliparodi/super-hero-api.git'

                // Run Maven on a Unix agent.
                sh "mvn test"
            }

            post {
                success {
                    echo "test passed"
                    script {
                        withCredentials([string(credentialsId: 'CODECOV_TOKEN', variable: 'CODECOV_TOKEN')]) {
                            sh 'docker run --rm -v $PWD:/tmp -e CODECOV_TOKEN=$CODECOV_TOKEN codecov/codecov:latest'
                        }
                    }
                }
                failure  {
                    echo "test failed"
                    script {
                        withCredentials([string(credentialsId: 'CODECOV_TOKEN', variable: 'CODECOV_TOKEN')]) {
                            sh 'docker run --rm -v $PWD:/tmp -e CODECOV_TOKEN=$CODECOV_TOKEN codecov/codecov:latest'
                        }
                    }
                }
            }
        }
    }
}
