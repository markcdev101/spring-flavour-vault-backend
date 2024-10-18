pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'  // Your DockerHub credentials ID
        DOCKER_IMAGE_NAME = 'markcdev101docker/d3k7m5s9r4p2g2'  // Your Docker image name
        GIT_BRANCH = 'develop'  // Specify the branch to build (e.g., main or develop)
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the specified branch
                checkout scm: [$class: 'GitSCM', 
                               branches: [[name: "*/${GIT_BRANCH}"]],
                               userRemoteConfigs: [[
                                   url: 'https://github.com/markcdev101/spring-flavour-vault-backend.git',
                                   credentialsId: 'github-credentials'
                               ]]]
            }
        }

        stage('Build with Maven') {
            steps {
                // Run Maven clean and package to build the Spring Boot app, skipping tests
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build the Docker image, tag it with the Jenkins build number
                    docker.build("${DOCKER_IMAGE_NAME}:${env.BUILD_ID}")
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    // Log in to DockerHub and push the built image
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        docker.image("${DOCKER_IMAGE_NAME}:${env.BUILD_ID}").push()
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Build and Push Successful!'
        }
        failure {
            echo 'Build Failed'
        }
    }
}
