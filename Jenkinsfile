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
        stage('4th Stage Building Docker Image') {
            steps {
                 script {
                     sh 'mvn clean install && docker build -t orkhan2000/phone-app-backend .'
                 }
            }
        }
        stage('5th Deploying to Docker Hub') {
            steps {
                 script {
                     withCredentials([usernamePassword(
                       credentialsId: 'dockerhubpwd',
                       usernameVariable: 'orkhan2000',
                       passwordVariable: 'orkhan197324'
                     )]
                     ) {
                         sh 'docker login -u orkhan2000 -p orkhan197324'
                     }
                     sh 'docker push  orkhan2000/phone-app-backend'
                 }
            }
        }
    }
}