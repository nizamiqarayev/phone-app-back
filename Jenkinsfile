pipeline {
    agent any
    stages {
        stage ('1st Stage Compiling') {
            steps {
                withMaven(maven: 'maven-3') {
                    sh 'mvn clean compile'
                }
            }
        }
        stage ('2nd Stage Testing') {
            steps {
                withMaven(maven: 'maven-3') {
                    sh 'mvn test'
                }
            }
        }
        stage ('3rd Stage SonarQube analysing') {
            steps {
                withMaven(maven: 'maven-3') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar'
                    }
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