def call(String url, String branch) {
    echo "Checking out the code from GitHub"
    git url: url, branch: branch
    echo "Done!"
}
