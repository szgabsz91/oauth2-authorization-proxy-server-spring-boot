#!/usr/bin/env bash

docker stop mongo
docker rm mongo
docker run --name mongo -d -p 127.0.0.1:27017:27017 mongo:6.0.3
