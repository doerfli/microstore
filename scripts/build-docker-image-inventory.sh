#!/bin/bash
set -e

docker build --build-arg component=inventory --build-arg mainclass=li.doerf.microstore.inventory.InventoryApplicationKt -t doerfli/microstore-inventory-svc:latest .
docker push doerfli/microstore-inventory-svc:latest

