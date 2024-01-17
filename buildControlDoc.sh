#!/usr/bin/bash

APK_PATH=$1
git clone https://github.com/XaverianTeamRobotics/FTC-DocParser
cd FTC-DocParser || exit 1
cp ../"$APK_PATH" ./APK.apk
./unzipAPK.sh APK.apk
./gradlew run --no-daemon
cp -r ./doc-out/ ../doc-out/
cd ..
rm -rf FTC-DocParser