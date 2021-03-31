#!/usr/bin/env groovy

def call() {
    sh 'npm version patch'
}

