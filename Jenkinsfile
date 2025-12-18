pipeline {
    agent any

    tools {
        maven "M2_HOME"
    }

    environment {
        DOCKER_CREDENTIALS = credentials('d431a208-50e1-4ea5-adbd-34520e3f242b')
        IMAGE_TAG = "soraexplora/student-management:${BUILD_NUMBER}"
        EXPECTED_DB_URL = "jdbc:mysql://10.255.255.254:3306/springdb"
    }

    stages {

        stage("Code Checkout") {
            steps {
                deleteDir() // clean workspace to avoid stale files
                git branch: 'main',
                    url: 'https://github.com/SoraExplora/Student_management.git'
                sh "echo 'Current application.properties:'"
                sh "cat src/main/resources/application.properties"
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
                // Debug: check JAR contents
                sh "ls -lh target/"
                sh "echo 'application.properties in JAR:'"
                sh "unzip -p target/student-management-0.0.1-SNAPSHOT.jar BOOT-INF/classes/application.properties"

                // Sanity check: fail build if DB URL is wrong
                script {
                    def dbUrl = sh(
                        script: "unzip -p target/student-management-0.0.1-SNAPSHOT.jar BOOT-INF/classes/application.properties | grep 'spring.datasource.url' | cut -d'=' -f2",
                        returnStdout: true
                    ).trim()

                    if (!dbUrl.contains(env.EXPECTED_DB_URL)) {
                        error "ERROR: application.properties contains wrong DB URL: ${dbUrl}. Expected: ${env.EXPECTED_DB_URL}"
                    }
                }
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
