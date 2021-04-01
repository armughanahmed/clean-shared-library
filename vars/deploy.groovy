#!/usr/bin/env groovy

import com.example.Docker

def call(String packageJSON) {
    return new Docker(this).deploy(packageJSON)
}
