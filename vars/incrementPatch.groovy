#!/usr/bin/env groovy

def call() {
    nodejs(nodeJSInstallationName: 'node') {
       this.sh 'npm version patch'
     }
}

