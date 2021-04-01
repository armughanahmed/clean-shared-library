#!/usr/bin/env groovy
package com.example

class Docker implements Serializable {

    def script

    Docker(script) {
        this.script = script
    }

    def buildDockerImage(String packageJSON) {
        script.withCredentials([script.usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
            script.docker.withRegistry('https://registry.hub.docker.com', 'dockerhub') {
                script.docker.build("${script.env.IMAGE_NAME}:${packageJSON}", '.').push()
            }
        }
    }
    def deploy() {
        echo 'deploying docker image to EC2...'

        def shellCmd = "bash ./server-cmds.sh ${script.env.IMAGE_NAME}"

        sshagent(['ec2-server-key']) {
            sh "scp server-cmds.sh ${script.env.EC2_IP}:/home/ec2-user"
            //    sh "scp docker-compose.yaml ${ec2Instance}:/home/ec2-user"
            sh "ssh -o StrictHostKeyChecking=no ${script.env.EC2_IP} ${shellCmd}"
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

