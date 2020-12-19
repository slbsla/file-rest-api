pipeline {
 agent any
 tools {
         maven 'Maven3'
         jdk 'java 8'
 }
 stages  {
      stage ('Welcome message') {
         steps {
               bat 'echo "Hi, let s start !"'
              }
       }
     stage ('Clear Project') {
        steps {
              sh 'mvn clean'
             }
      }
     stage ('Compile Project') {
        steps {
                  sh 'mvn compile'
         }
      }
	 stage ('Test') {
        steps {
               sh 'mvn test'
         }
      }
	  stage ('Generating war') {
        steps {
             sh 'mvn package'
          }
      }
  }
}