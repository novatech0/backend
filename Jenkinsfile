pipeline {
  agent any

  environment {
    DOCKER_IMAGE = 'pierod04/agrotech-backend'
  }

  stages {
    stage('Checkout del código') {
      steps {
        checkout scm
      }
    }

    stage('Preparar entorno') {
      steps {
        sh 'java -version'
        sh 'mvn -v'
        sh 'docker --version'
      }
    }

    stage('Compilar proyecto con tests') {
      steps {
        withCredentials([
          string(credentialsId: 'MYSQL_HOST', variable: 'MYSQL_HOST'),
          string(credentialsId: 'MYSQL_PORT', variable: 'MYSQL_PORT'),
          string(credentialsId: 'MYSQL_USER', variable: 'MYSQL_USER'),
          string(credentialsId: 'MYSQL_PASSWORD', variable: 'MYSQL_PASSWORD'),
          string(credentialsId: 'MYSQL_DATABASE', variable: 'MYSQL_DATABASE'),
          string(credentialsId: 'JWT_SECRET', variable: 'JWT_SECRET')
        ]) {
          sh 'mvn clean package -DskipTests=false'
        }
      }
    }

    stage('Construir imagen Docker') {
      steps {
        script {
          sh 'docker build -t $DOCKER_IMAGE .'
        }
      }
    }

    stage('Push a Docker Hub') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'docker-hub', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh '''
            echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin
            docker push $DOCKER_IMAGE
          '''
        }
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
