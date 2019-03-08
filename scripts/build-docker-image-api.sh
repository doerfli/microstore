#!/bin/bash
set -e

export component=api
export mainclass=li.doerf.microstore.api.ApiApplicationKt

docker build -t doerfli/microstore-api-svc:latest -f Dockerfile-api .
docker push doerfli/microstore-api-svc:latest
