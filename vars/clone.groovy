def(url,branch){
  echo "checking out the code from github"
  git url: "$(url)", branch: "$(branch)"
  echo "done!"
}
