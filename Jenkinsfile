pipeline {
 agent any
 tools {
         maven 'Maven3'
         jdk 'java 8'
 }
 stages  {
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

	  stage ('Deploying on tomcat') {
        steps {
            deploy adapters: [tomcat9(credentialsId: 'admin', path: '', url: 'http://localhost:8014/')], contextPath: null, war: 'target/api-file.war'
          }
      }

  }
}