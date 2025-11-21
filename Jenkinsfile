pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    stages {
        stage("Code Checkout") {
            steps {
                git branch: 'main',
                    url: 'https://github.com/NotSoHealthy/ben_hamouda_mohamed_amin_4SAE9.git'
            }
        }

        stage('Code Test') {
            steps {
                sh "mvn test"
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
                    sh "docker build -t student-management:1.0 ."
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    sh "docker push NotSoHealthy/student-management:1.0:1.0"
                }
            }
        }
    }

    post {
        always {
            echo "Pipeline finished!"
        }
    }
}
