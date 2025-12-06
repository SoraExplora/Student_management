pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        DOCKER_IMAGE = "soraexplora/student-management:1.0"
        SONAR_TOKEN = credentials('sonar-token')
    }

    stages {
        stage("Checkout") {
            steps {
                git branch: 'main', url: 'https://github.com/SoraExplora/Student_management.git'
            }
        }

        stage("Test & Coverage") {
            steps {
                sh "mvn clean test jacoco:report"
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage("SonarQube Analysis") {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh """
                        mvn sonar:sonar \
                        -Dsonar.login=$SONAR_TOKEN \
                        -Dsonar.java.coveragePlugin=jacoco \
                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    """
                }
            }
        }

        stage("Création Livrable") {
            steps {
                sh "mvn package -DskipTests"
            }
            post {
                success {
                    archiveArtifacts 'target/*.jar'
                }
            }
        }

        stage("Docker Build & Push") {
            when { expression { fileExists('Dockerfile') } }
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                    withCredentials([usernamePassword(
                        credentialsId: 'd431a208-50e1-4ea5-adbd-34520e3f242b', 
                        usernameVariable: 'DOCKER_USER', 
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
        }

        /* 🔥 NEW STAGE: Deploy to Kubernetes */
        stage("Deploy to Kubernetes") {
            steps {
                withCredentials([string(credentialsId: 'K8S_TOKEN', variable: 'TOKEN')]) {
                    sh """
                        # Configure kubectl for Jenkins using service account token
                        kubectl config set-cluster k8s-cluster --server=https://https://127.0.0.1:32771 --certificate-authority=/tmp/ca.crt --embed-certs=true
                        kubectl config set-credentials jenkins --token=$TOKEN
                        kubectl config set-context jenkins-context --cluster=k8s-cluster --user=jenkins
                        kubectl config use-context jenkins-context

                        # Apply Kubernetes manifests
                        kubectl apply -f k8s/mysql-deployment.yaml -n devops
                        kubectl apply -f k8s/configmap-secret.yaml -n devops
                        kubectl apply -f k8s/spring-deployment.yaml -n devops
                        kubectl apply -f k8s/spring-service.yaml -n devops
                    """
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
