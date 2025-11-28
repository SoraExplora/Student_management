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
        stage("Code Checkout") {
            steps {
                git branch: 'main',
                    url: 'https://github.com/SoraExplora/Student_management.git'
            }
        }

        stage('Code Test & Coverage') {
            steps {
                sh "mvn clean test jacoco:report"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.java.binaries=target/classes -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    timeout(time: 15, unit: 'MINUTES') {  // Increased timeout
                        waitForQualityGate abortPipeline: true
                    }
                }
            }
        }

        stage('Code Build') {
            steps {
                sh "mvn package -DskipTests"  // Skip tests since they already ran
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Check if Dockerfile exists before building
                    sh '''
                        if [ -f "Dockerfile" ]; then
                            docker build -t ${DOCKER_IMAGE} .
                        else
                            echo "No Dockerfile found, skipping Docker build"
                            exit 0
                        fi
                    '''
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    // Only push if Docker build was successful
                    sh '''
                        if [ -f "Dockerfile" ]; then
                            docker login -u $DOCKER_USER -p $DOCKER_PASS
                            docker push ${DOCKER_IMAGE}
                        else
                            echo "No Docker image to push"
                        fi
                    '''
                }
            }
        }
    }

    post {
        always {
            // Clean up workspace
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
