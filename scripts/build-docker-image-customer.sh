#!/bin/bash
set -e 

docker build --build-arg component=customer --build-arg mainclass=li.doerf.microstore.customer.CustomerApplicationKt -t doerfli/microstore-customer-svc:latest .
docker push doerfli/microstore-customer-svc:latest

