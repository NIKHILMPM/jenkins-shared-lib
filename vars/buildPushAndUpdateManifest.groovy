def call(String githubCredentialsId,
         String dockerCredentialsId,
         String dockerRepo,
         String imageName,
         String tag) {

    def fullImage = "${dockerRepo}/argocd-${imageName}:${tag}"

    // Build Docker Image
    sh "docker build -t ${fullImage} ./${imageName}"

    // Push Docker Image
    withDockerRegistry(credentialsId: dockerCredentialsId) {
        sh "docker push ${fullImage}"
    }

    // Update K8s Manifest + Push to GitHub
    withCredentials([usernamePassword(
        credentialsId: githubCredentialsId,
        usernameVariable: 'GIT_USERNAME',
        passwordVariable: 'GIT_TOKEN'
    )]) {

        sh """
            sed -i "s|image: .*|image: ${fullImage}|g" k8s/${imageName}-manifest.yaml

            git config user.name "jenkins"
            git config user.email "jenkins@ci.com"

            git add k8s/${imageName}-manifest.yaml

            if ! git diff --cached --quiet; then
                git commit -m "[skip ci] Update ${imageName} image to ${tag}"
                git push https://${GIT_USERNAME}:${GIT_TOKEN}@github.com/NIKHILMPM/devops-argocd.git ${env.BRANCH_NAME}
            else
                echo "No manifest changes detected"
            fi
        """
    }
}
