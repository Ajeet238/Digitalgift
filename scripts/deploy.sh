#!/bin/bash

set -e

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
    *) echo "Unknown service: $1" && exit 1 ;;
  esac
}

LOG_FILE="/home/ubuntu/deploy.log"
echo "🚀 Starting microservice deployment at $(date)" | tee -a $LOG_FILE

for SERVICE in "${SERVICES[@]}"; do
  PORT=$(get_port "$SERVICE")
  IMAGE_TAG="$DOCKER_USER/$SERVICE:latest"

  echo -e "\n🔄 Deploying $SERVICE on port $PORT" | tee -a $LOG_FILE

  echo "🛑 Stopping and removing existing container (if any)..." | tee -a $LOG_FILE
  docker stop "$SERVICE" 2>/dev/null || true
  docker rm "$SERVICE" 2>/dev/null || true

  if [[ "$PORT" != "22" ]]; then
    echo "🔓 Releasing port $PORT if in use..." | tee -a $LOG_FILE
    fuser -k "$PORT"/tcp 2>/dev/null || true
  else
    echo "⚠️ Skipping port 22 (SSH) to avoid disconnect" | tee -a $LOG_FILE
  fi

  echo "📦 Checking if image $IMAGE_TAG already exists locally..." | tee -a $LOG_FILE
  if docker image inspect "$IMAGE_TAG" > /dev/null 2>&1; then
    echo "✅ Image found locally. Skipping pull." | tee -a $LOG_FILE
  else
    echo "📥 Pulling image $IMAGE_TAG..." | tee -a $LOG_FILE
    docker pull "$IMAGE_TAG" | tee -a $LOG_FILE
  fi

  echo "🚀 Starting container..." | tee -a $LOG_FILE
  CONTAINER_ID=$(docker run -d --name "$SERVICE" -p "$PORT:$PORT" "$IMAGE_TAG")
  echo "🆔 $SERVICE started with container ID: $CONTAINER_ID" | tee -a $LOG_FILE
done

echo -e "\n🧹 Cleaning up unused Docker images..." | tee -a $LOG_FILE
docker image prune -f > /dev/null

echo -e "\n📦 Running containers:" | tee -a $LOG_FILE
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}" | tee -a $LOG_FILE

echo -e "\n🎉 All services deployed successfully at $(date)\n" | tee -a $LOG_FILE
