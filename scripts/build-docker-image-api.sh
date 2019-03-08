#!/bin/bash
set -e

docker build -t doerfli/microstore-api-svc:latest -f Dockerfile-api .
docker push doerfli/microstore-api-svc:latest
