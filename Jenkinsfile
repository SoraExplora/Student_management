pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        DOCKER_IMAGE = "soraexplora/student-management:1.0"
        SONAR_TOKEN = credentials('sonar-token') // create token in SonarQube
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
                // Run tests with Jacoco agent
                sh "mvn clean test jacoco:report"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.java.binaries=target/classes -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Code Build') {
            steps {
                sh "mvn package"
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'd431a208-50e1-4ea5-adbd-34520e3f242b', 
                                                 usernameVariable: 'DOCKER_USER', 
                                                 passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }
    }
}
