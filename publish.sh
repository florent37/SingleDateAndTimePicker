#!/usr/bin/env bash
. ~/.bash_profile
./gradlew clean :singledateandtimepicker:assembleDebug
./gradlew :singledateandtimepicker:install :singledateandtimepicker:bintrayUpload