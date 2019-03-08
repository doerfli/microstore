#!/bin/bash
set -e 

export component=payment
export mainclass=li.doerf.microstore.payment.PaymentApplicationKt

docker build -t doerfli/microstore-payment-svc:latest -f Dockerfile-payment .
docker push doerfli/microstore-payment-svc:latest
