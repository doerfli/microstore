#!/bin/bash
set -e

docker build --build-arg component=api --build-arg mainclass=li.doerf.microstore.api.ApiApplicationKt -t doerfli/microstore-api-svc:latest -f Dockerfile .
docker push doerfli/microstore-api-svc:latest
