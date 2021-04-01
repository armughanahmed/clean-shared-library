#!/usr/bin/env groovy

def call() {
    nodejs(nodeJSInstallationName: 'node') {
       sh 'npm version minor'
     }
}
