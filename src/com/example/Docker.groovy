#!/usr/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String packageJSON) {
        def packageJSONVersion = packageJSON.version
        echo packageJSONVersion
        script.withCredentials([script.usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                script.docker.build("$script.env.IMAGE_NAME:$packageJSONVersion", '.').push()
            }
        }
    }
    def deploy(String packageJSON) {
        echo 'deploying docker image to EC2...'
        def tag = packageJSON.version
        def imageName = "$script.env.IMAGE_NAME:$tag"
        def shellCmd = "bash ./${script.env.SERVER_CMDS}.sh "
        def ec2Instance = "$script.env.EC2_IP"

        sshagent(['ec2-server-key']) {
            sh "scp server-cmds.sh ${ec2Instance}:/home/ec2-user"
            sh "scp docker-compose.yaml ${ec2Instance}:/home/ec2-user"
            sh "ssh -o StrictHostKeyChecking=no ${ec2Instance} ${shellCmd}"
        }
    }
    def commitUpdate() {
        withCredentials([usernamePassword(credentialsId: 'github', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            // git config here for the first time run
            sh 'git config --global user.email "jenkins@example.com"'
            sh 'git config --global user.name "jenkins"'

            sh "git remote set-url origin https://${USER}:${PASS}@github.com/armughanahmed/node-app.git"
            sh 'git add .'
            sh 'git commit -m "ci: version bump"'
            sh 'git push origin HEAD:jenkins-jobs'
        }
    }

}

