def call(String credentialsId, String u_name, String image, String tag) {

    withDockerRegistry(
        credentialsId: credentialsId,
        url: ''
    ) {
        sh "docker push ${u_name}/${image}:${tag}"
    }
}
