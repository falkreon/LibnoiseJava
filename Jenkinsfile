node {
	checkout scm
	sh './gradlew clean build sourceJar publishToMavenLocal'
	archive 'build/libs/*jar'
}
