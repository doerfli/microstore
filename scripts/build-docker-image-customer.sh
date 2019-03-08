#!/bin/bash
set -e 

export component=customer
export mainclass=li.doerf.microstore.customer.CustomerApplicationKt

docker build -t doerfli/microstore-customer-svc:latest -f Dockerfile .
docker push doerfli/microstore-customer-svc:latest

