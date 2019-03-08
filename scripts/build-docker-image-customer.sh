#!/bin/bash
set -e 

docker build -t doerfli/microstore-customer-svc:latest -f Dockerfile-customer .
docker push doerfli/microstore-customer-svc:latest

