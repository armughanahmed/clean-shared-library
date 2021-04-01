#!/usr/bin/env groovy

import com.example.Docker

def call(String packageJSON) {
    sh "echo ${packageJSON.version}"
    // return new Docker(this).buildDockerImage(packageJSON)
}
