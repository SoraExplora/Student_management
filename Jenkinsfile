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
                deleteDir() // ensure fresh workspace
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
                sh "ls -lh target/"
            }
        }

        stage("Docker Build") {
            steps {
                script {
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
                    sh "kubectl set image deployment/spring-app spring-app=${IMAGE_TAG} -n devops"
                    sh "kubectl rollout status deployment/spring-app -n devops"
                }
            }
        }
    }
}
