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
             bat 'mvn clean'
           }
      }
     stage ('Compile Project') {
        steps {
             bat 'mvn compile'
         }
      }
	 stage ('Test') {
        steps {
              bat 'mvn test'
           }
      }
	  stage ('Generating war') {
        steps {
              bat 'mvn package'
          }
      }
  }
}