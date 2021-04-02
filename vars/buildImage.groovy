#!/usr/bin/env groovy

import com.example.Docker

def call(String commitId) {
    return new Docker(this).buildDockerImage(commitId)
}
