#!/bin/bash
set -e 

docker build -t doerfli/microstore-payment-svc:latest -f Dockerfile-payment .
docker push doerfli/microstore-payment-svc:latest
