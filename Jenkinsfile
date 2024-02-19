pipeline {
    agent any
    tools {
        // Install the Maven version configured as "M3" and add it to the path.
        maven "M3"
    }

    stages {
        stage('Checkout') {
            steps {
                // Get some code from a GitHub repository
                checkout([$class: 'GitSCM', branches: [[name: $GIT_BRANCH]], userRemoteConfigs: [[url: 'https://github.com/Juliparodi/super-hero-api.git']]])
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
                    echo "test passed :)"
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                success {
                    echo "test failed :("
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
            }
        }
    }
}
