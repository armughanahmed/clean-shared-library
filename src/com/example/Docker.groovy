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
                script.docker.build("${script.env.IMAGE_NAME}:${packageJSON}-${script.BUILD_NUMBER}", '.').push()
            }
        }
    }
    def deploy() {
        def shellCmd = "bash ./server-cmds.sh ${script.env.IMAGE_NAME}"

        script.sshagent(['ec2-server-key']) {
            script.sh "scp server-cmds.sh ${script.env.EC2_IP}:/home/ec2-user"
            script.sh "ssh -o StrictHostKeyChecking=no ${script.env.EC2_IP} ${shellCmd}"
        }
    }

}

