#!/bin/bash
set -e 

docker build -t doerfli/microstore-inventory-svc:latest -f Dockerfile-inventory .
docker push doerfli/microstore-inventory-svc:latest

