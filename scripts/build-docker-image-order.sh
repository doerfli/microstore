#!/bin/bash
set -e 

docker build --build-arg component=order --build-arg mainclass=li.doerf.microstore.order.OrderApplicationKt -t doerfli/microstore-order-svc:latest .
docker push doerfli/microstore-order-svc:latest

