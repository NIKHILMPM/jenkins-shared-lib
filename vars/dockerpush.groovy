def call(String credentialsId, String image, String tag) {

    withDockerRegistry(
        credentialsId: credentialsId,
        url: ''
    ) {
        sh "docker push ${image}:${tag}"
    }
}
