#!/bin/bash
set -e

docker build -t doerfli/microstore-frontend:latest -f frontend/Dockerfile frontend
docker push doerfli/microstore-frontend:latest
