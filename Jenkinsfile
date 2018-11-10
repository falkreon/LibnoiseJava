node {
	checkout scm
	sh './gradlew clean build sourceJar deploy'
	archive 'build/libs/*jar'
}
