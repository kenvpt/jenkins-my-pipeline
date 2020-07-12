node{
    stage("Pull Repo"){
        echo "hello"   
    }
    stage("Build Image"){
        sh "packer version"
    }
    stage("Send Notification to Slack"){
        echo "hello"
    }
    stage("Send Email"){
        echo "hello"
    }
}