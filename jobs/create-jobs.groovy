pipelineJob('CI_CD_Pipeline') {
definition {
    cps {
    script(
        pipeline {
        agent any
        environment {
            DOCKER_IMAGE = "devops2k23/krista-inetrn:v3"
        }
        stages {
            stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/Sagar-kedar/jenkin-pipeline.git'
            }
            }
            stage('Build Docker Image') {
            steps {
                sh 'docker build -t $DOCKER_IMAGE .'
            }
            }
            stage('Push Image to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-secret', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                sh 'docker login -u $DOCKER_USER -p $DOCKER_PASS'
                sh 'docker push $DOCKER_IMAGE'
                }
            }
            }
            stage('Deploy to Minikube') {
            steps {
                sh 'kubectl apply -f k8-deployment.yaml'
            }
            }
        }
        }
    )
    }
}
}