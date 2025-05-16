#!/bin/bash

DOCKER_USER=ajeetkumar238199
SERVICES=("userservice" "filestorage" "filemetadata" "apigateway" "giftservice" "postapi")

get_port() {
  case "$1" in
    userservice) echo 8081 ;;
    filestorage) echo 8082 ;;
    filemetadata) echo 8083 ;;
    giftservice) echo 8084 ;;
    postapi) echo 8087 ;;
    apigateway) echo 9090 ;;
  esac
}

for SERVICE in "${SERVICES[@]}"; do
  PORT=$(get_port $SERVICE)
  echo "Deploying $SERVICE on port $PORT"
  docker pull $DOCKER_USER/$SERVICE:latest
  docker stop $SERVICE || true
  docker rm $SERVICE || true
  docker run -d --name $SERVICE -p $PORT:$PORT $DOCKER_USER/$SERVICE
done
