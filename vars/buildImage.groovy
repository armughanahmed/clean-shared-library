#!/usr/bin/env groovy

import com.example.Docker

def call(String packageJSON) {
    // sh "echo ${packageJSON}"
    sh 'echo $(grep version package.json | awk -F \" '{print $4}')'
    // return new Docker(this).buildDockerImage(packageJSON)
}
