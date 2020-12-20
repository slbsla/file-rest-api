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
             bat 'mvn compile -Dmaven.test.skip=true'
         }
      }
	 stage ('Test') {
        steps {
              bat 'mvn test'
           }
      }
      stage ('Sonar/Qualite') {
           steps {
                    bat 'mvn sonar:sonar'
           }
      }

      /*stage('Sonarqube') {
          environment {
              scannerHome = tool 'SonarQubeScanner'
          }
          steps {
              withSonarQubeEnv('sonarqube') {
                  bat 'D:/Tools/sonarqube-7.6/bin/windows-x86-64/StartSonar'
              }
              timeout(time: 10, unit: 'MINUTES') {
                  waitForQualityGate abortPipeline: true
              }
          }
      }*/
	  stage ('Generating war') {
        steps {
              bat 'mvn package'
          }
      }

	  stage ('Deploying on tomcat') {
        steps {
            deploy adapters: [tomcat7(credentialsId: 'admin_tomcat', path: '', url: 'http://localhost:8014')], contextPath: null, war: 'target/api-file.war'
          }
      }

  }
}