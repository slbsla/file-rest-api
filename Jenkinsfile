pipeline {
 agent any
 stages  {
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