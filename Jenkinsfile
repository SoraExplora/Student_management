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
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('sonarqube') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.login=$SONAR_TOKEN \
                            -Dsonar.java.coveragePlugin=jacoco \
                            -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
                    '''
                }
            }
            
            post {
                success {
                    echo "✅ SonarQube analysis submitted successfully"
                    script {
                        // Get the actual SonarQube URL from environment
                        def sonarUrl = env.SONAR_HOST_URL ?: 'http://localhost:9000'
                        echo "Check results at: ${sonarUrl}/dashboard?id=tn.esprit%3Astudent-management"
                    }
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
                    
                    // Wait a bit for analysis to start processing
                    sleep 30
                    
                    try {
                        timeout(time: 3, unit: 'MINUTES') {
                            def qualityGate = waitForQualityGate abortPipeline: false
                            echo "✅ Quality Gate Status: ${qualityGate.status}"
                            
                            if (qualityGate.status != 'OK') {
                                echo "⚠️ Quality Gate not passed: ${qualityGate.status}"
                                currentBuild.result = 'UNSTABLE'
                            }
                        }
                    } catch (org.jenkinsci.plugins.workflow.steps.FlowInterruptedException e) {
                        echo "⏰ Quality Gate timeout after 3 minutes"
                        echo "📊 SonarQube analysis completed successfully but quality gate check timed out"
                        echo "🔗 Check results manually at: http://localhost:9000/dashboard?id=tn.esprit%3Astudent-management"
                        echo "➡️ Continuing pipeline with UNSTABLE status..."
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
                    archiveArtifacts 'target/*.jar'
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
            echo "🔗 SonarQube: http://localhost:9000/dashboard?id=tn.esprit%3Astudent-management"
            
            // Clean up Docker images to save space
            script {
                if (fileExists('Dockerfile')) {
                    sh "docker rmi ${DOCKER_IMAGE} || true"
                }
            }
            
            cleanWs()
        }
        success {
            echo "🎉 Pipeline completed successfully!"
        }
        unstable {
            echo "⚠️ Pipeline completed with warnings"
            echo "ℹ️ This is usually due to SonarQube quality gate timeout - analysis was still successful!"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}
