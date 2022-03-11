pipeline {
    agent any
    tools {
        maven 'apache-maven-3.8.4'
        }
    stages {
        stage ('1st Stage Compiling') {
            steps {
                    sh 'mvn clean compile'
            }
        }
        stage ('2nd Stage Testing') {
            steps {
                    sh 'mvn test'
            }
        }
        stage ('3rd Stage SonarQube analysing') {
            steps {
                    withSonarQubeEnv(installationName: 'sq1') {
                        sh 'mvn clean verify sonar:sonar'
                }
                waitForQualityGate abortPipeline: true
            }
        }
        stage('4th Stage Building Docker Image') {
            steps {
                 script {
                     sh 'docker build -t orkhan2000/phone-app-backend .'
                 }
            }
        }
        stage('5th Deploying to Docker Hub') {
            steps {
                 script {
                     withCredentials([string(credentialsId: 'Docker-Hub-Password', variable: 'dockerhubpwd')]) {
                         sh 'docker login -u orkhan2000 -p ${dockerhubpwd}'
                     }
                     sh 'docker push  orkhan2000/phone-app-backend'
                 }
            }
        }
    }
}