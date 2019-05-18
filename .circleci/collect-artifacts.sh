#!/usr/bin/env sh

DESTINATION_FOLDER=~/oauth2-authorization-proxy-server-spring-boot/build/libs
rm -rf ${DESTINATION_FOLDER}
mkdir -p ${DESTINATION_FOLDER}

for buildFolder in $(find . -type d -name "build"); do
    libsFolder="$buildFolder/libs"

    if [[ -d "$libsFolder" ]]; then
        cp ${libsFolder}/*.jar ${DESTINATION_FOLDER}
    fi
done
