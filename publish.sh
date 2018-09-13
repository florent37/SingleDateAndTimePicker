#!/usr/bin/env bash
. ~/.bash_profile
./gradlew clean assembleDebug
./gradlew install bintrayUpload