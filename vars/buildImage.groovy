#!/usr/bin/env groovy

import com.example.Docker

def call(String packageJSON) {
    // sh "echo ${packageJSON}"
    sh "echo ${packageJSON[0]}"
    // return new Docker(this).buildDockerImage(packageJSON)
}
