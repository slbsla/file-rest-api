pipeline {
 agent any
 stages  {
     stage ('Clear Project') {
        steps {
            withMaven(maven : 'Maven3')    {
                  sh 'mvn clean'
                 }
             }
      }
     stage ('Compile Project') {
        steps {
            withMaven(maven : 'Maven3')    {
                  sh 'mvn compile'
                 }
             }
      }
	 stage ('Test') {
        steps {
            withMaven(maven : 'Maven3')    {
                  sh 'mvn test'
                 }
             }
      }
	  stage ('Generating war') {
        steps {
            withMaven(maven : 'Maven3')    {
                  sh 'mvn package'
                 }
             }
      }
  }
}