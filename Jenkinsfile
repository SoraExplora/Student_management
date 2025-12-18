pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        DOCKER_CREDENTIALS = credentials('d431a208-50e1-4ea5-adbd-34520e3f242b')
        IMAGE_TAG = "soraexplora/student-management:${BUILD_NUMBER}"
    }

    stages {

        stage("Code Checkout") {
            steps {
                // Force a fresh clone to avoid stale workspace issues
                deleteDir()
                git branch: 'main',
                    url: 'https://github.com/SoraExplora/Student_management.git'
            }
        }

        stage("Code Test") {
            steps {
                sh "mvn clean test"
            }
        }

        stage("Code Build") {
            steps {
                sh "mvn clean package -DskipTests"
                // Check the JAR and application.properties
                sh "ls -lh target/"
                sh "unzip -l target/student-management-0.0.1-SNAPSHOT.jar | grep application.properties"
            }
        }

        stage("Docker Build") {
            steps {
                script {
                    // Build Docker image without using cache
                    sh "docker build --no-cache -t ${IMAGE_TAG} ."
                }
            }
        }

        stage("Docker Push") {
            steps {
                script {
                    sh 'echo $DOCKER_CREDENTIALS_PSW | docker login -u $DOCKER_CREDENTIALS_USR --password-stdin'
                    sh "docker push ${IMAGE_TAG}"
                }
            }
        }

        stage("Deploy to Kubernetes") {
            steps {
                script {
                    // Use latest image tag for deployment
                    sh "kubectl set image deployment/spring-app spring-app=${IMAGE_TAG} -n devops"
                    sh "kubectl rollout status deployment/spring-app -n devops"
                }
            }
        }
    }
}
