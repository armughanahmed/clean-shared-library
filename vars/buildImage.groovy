#!/usr/bin/env groovy

import com.example.Docker

def call(String packageJSON) {
    sh "echo ${packageJSON}"
    // return new Docker(this).buildDockerImage(packageJSON)
}
