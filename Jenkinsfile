pipeline {
  agent {
    node {
      label 'android-f25'
    }
    
  }
  stages {
    stage('checkout') {
      steps {
        git(branch: 'Jenkinstest', credentialsId: 'git yona', url: 'https://github.com/yonadev/yona-app-android.git', changelog: true)
      }
    }
    stage('gradle step') {
      steps {
        sh 'android list sdk -e -a'
        sh 'echo \"y\" | ${ANDROID_HOME}/tools/android --verbose update sdk --no-ui --all --filter platform-tools,android-27,extra-android-m2repository'
        sh 'android list sdk -e -a'
        sh 'ls -l ${ANDROID_HOME}/tools'
        sh './gradlew --no-daemon -Dorg.gradle.jvmargs=-Xmx1536m app:assembleDebug --scan'
      }
    }
    stage('Build') {
      steps {
        archiveArtifacts(artifacts: 'app/build/outputs/apk/*.apk', allowEmptyArchive: true)
      }
    }
  }
}
