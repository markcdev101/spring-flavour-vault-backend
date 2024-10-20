pipeline {
    agent any

    environment {
        DOCKER_CREDENTIALS_ID = 'dockerhub-credentials'  // Your DockerHub credentials ID
        DOCKER_IMAGE_NAME = 'markcdev101docker/d3k7m5s9r4p2g2'  // Base Docker image name
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
                    def imageTag = env.BUILD_ID  // Default image tag is the build ID
                    def branchName = env.GIT_BRANCH

                    // Check if the branch is not 'develop' and append 'ftr' to the image tag
                    if (branchName != 'develop') {
                        imageTag = "${branchName}-${env.BUILD_ID}-ftr"
                    }

                    // Build the Docker image with the appropriate tag
                    docker.build("${DOCKER_IMAGE_NAME}:${imageTag}")
                }
            }
        }

        stage('Push Docker Image to DockerHub') {
            steps {
                script {
                    def imageTag = env.BUILD_ID  // Default image tag is the build ID
                    def branchName = env.GIT_BRANCH

                    // Check if the branch is not 'develop' and append 'ftr' to the image tag
                    if (branchName != 'develop') {
                        imageTag = "${branchName}-${env.BUILD_ID}-ftr"
                    }

                    // Log in to DockerHub and push the built image
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKER_CREDENTIALS_ID}") {
                        docker.image("${DOCKER_IMAGE_NAME}:${imageTag}").push()
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
