#!/bin/bash
set -e

docker build --build-arg component=payment --build-arg mainclass=li.doerf.microstore.payment.PaymentApplicationKt -t doerfli/microstore-payment-svc:latest -f Dockerfile .
docker push doerfli/microstore-payment-svc:latest
