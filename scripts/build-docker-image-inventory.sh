#!/bin/bash
set -e 

export component=inventory
export mainclass=li.doerf.microstore.inventory.InventoryApplicationKt

docker build -t doerfli/microstore-inventory-svc:latest -f Dockerfile-inventory .
docker push doerfli/microstore-inventory-svc:latest

