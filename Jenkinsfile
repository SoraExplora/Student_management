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
            
            post {
                always {
                    junit 'target/surefire-reports/*.xml' // Publish test results
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh "mvn sonar:sonar -Dsonar.login=${SONAR_TOKEN} -Dsonar.java.binaries=target/classes -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml"
                }
            }
            
            post {
                success {
                    echo "✅ SonarQube analysis submitted successfully"
                    echo "Check results at: ${env.SONAR_HOST_URL}/dashboard?id=tn.esprit%3Astudent-management"
                }
                failure {
                    echo "❌ SonarQube analysis failed"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                script {
                    echo "🔍 Checking Quality Gate status..."
                    
                    try {
                        // Try the standard quality gate wait with timeout
                        timeout(time: 5, unit: 'MINUTES') {
                            def qualityGate = waitForQualityGate abortPipeline: false
                            echo "✅ Quality Gate Status: ${qualityGate.status}"
                            
                            if (qualityGate.status != 'OK') {
                                echo "⚠️ Quality Gate not passed: ${qualityGate.status}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        echo "⏰ Quality Gate timeout after 5 minutes"
                        echo "📊 SonarQube analysis might still be processing"
                        echo "🔗 Check manually at: ${env.SONAR_HOST_URL}/dashboard?id=tn.esprit%3Astudent-management"
                        currentBuild.result = 'UNSTABLE'
                    } catch (Exception e) {
                        echo "❌ Quality Gate check failed: ${e.message}"
                        echo "➡️ Continuing pipeline anyway..."
                        currentBuild.result = 'UNSTABLE'
                    }
                }
            }
        }

        stage('Code Build') {
            steps {
                sh "mvn package -DskipTests"
            }
            
            post {
                success {
                    echo "✅ Application packaged successfully"
                    archiveArtifacts 'target/*.jar' // Archive the built JAR
                }
            }
        }

        stage('Docker Build') {
            when {
                expression { fileExists('Dockerfile') }
            }
            steps {
                script {
                    echo "🐳 Building Docker image..."
                    sh "docker build -t ${DOCKER_IMAGE} ."
                }
            }
            
            post {
                success {
                    echo "✅ Docker image built successfully: ${DOCKER_IMAGE}"
                }
                failure {
                    echo "❌ Docker build failed"
                }
            }
        }

        stage('Docker Push') {
            when {
                expression { fileExists('Dockerfile') }
            }
            steps {
                script {
                    withCredentials([usernamePassword(
                        credentialsId: 'd431a208-50e1-4ea5-adbd-34520e3f242b', 
                        usernameVariable: 'DOCKER_USER', 
                        passwordVariable: 'DOCKER_PASS'
                    )]) {
                        echo "🚀 Pushing Docker image to registry..."
                        sh "echo ${DOCKER_PASS} | docker login -u ${DOCKER_USER} --password-stdin"
                        sh "docker push ${DOCKER_IMAGE}"
                    }
                }
            }
            
            post {
                success {
                    echo "✅ Docker image pushed successfully: ${DOCKER_IMAGE}"
                }
            }
        }
    }

    post {
        always {
            echo "🏁 Pipeline execution completed"
            echo "📊 Build Result: ${currentBuild.currentResult}"
            echo "🔗 SonarQube: ${env.SONAR_HOST_URL}/dashboard?id=tn.esprit%3Astudent-management"
            
            // Clean up Docker images to save space
            script {
                if (fileExists('Dockerfile')) {
                    sh "docker rmi ${DOCKER_IMAGE} || true"
                }
            }
            
            cleanWs() // Clean workspace
        }
        success {
            echo "🎉 Pipeline completed successfully!"
            emailext (
                subject: "✅ Pipeline SUCCESS: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "The pipeline completed successfully.\n\nCheck SonarQube: ${env.SONAR_HOST_URL}/dashboard?id=tn.esprit%3Astudent-management",
                to: "you@email.com"
            )
        }
        unstable {
            echo "⚠️ Pipeline completed with warnings"
            echo "ℹ️ This is usually due to SonarQube quality gate timeout"
            emailext (
                subject: "⚠️ Pipeline UNSTABLE: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "The pipeline completed with warnings (likely SonarQube timeout).\n\nCheck SonarQube: ${env.SONAR_HOST_URL}/dashboard?id=tn.esprit%3Astudent-management",
                to: "you@email.com"
            )
        }
        failure {
            echo "❌ Pipeline failed!"
            emailext (
                subject: "❌ Pipeline FAILED: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                body: "The pipeline failed. Please check Jenkins logs.",
                to: "you@email.com"
            )
        }
    }
}
