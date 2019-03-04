#!/bin/bash
set -e 

docker build -t doerfli/microstore-customer-svc:latest -f Dockerfile-customer .
docker push doerfli/microstore-customer-svc:latest

docker build -t doerfli/microstore-payment-svc:latest -f Dockerfile-payment .
docker push doerfli/microstore-payment-svc:latest

docker build -t doerfli/microstore-inventory-svc:latest -f Dockerfile-inventory .
docker push doerfli/microstore-inventory-svc:latest

docker build -t doerfli/microstore-api-svc:latest -f Dockerfile-api .
docker push doerfli/microstore-api-svc:latest
