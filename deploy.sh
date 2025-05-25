#!/bin/bash

set -e

DOCKER_USER=ajeetkumar238199
SERVICES=("userservice" "filestorage" "filemetadata" "apigateway" "giftservice" "postapi" "serviceregistry")

get_port() {
  case "$1" in
	serviceregistry) echo 8761
    userservice) echo 8081 ;;
    filestorage) echo 8082 ;;
    filemetadata) echo 8088 ;;
    giftservice) echo 8084 ;;
    postapi) echo 8087 ;;
    apigateway) echo 9090 ;;
    *) echo "Unknown service: $1" && exit 1 ;;
  esac
}

LOG_FILE="/home/ubuntu/deploy.log"
echo -e "\n🚀 Deploy started at $(date)" | tee -a $LOG_FILE

for SERVICE in "${SERVICES[@]}"; do
  PORT=$(get_port "$SERVICE")
  IMAGE_TAG="$DOCKER_USER/$SERVICE:latest"

  echo -e "\n🔄 Deploying $SERVICE on port $PORT" | tee -a $LOG_FILE

  echo "🛑 Stopping and removing existing container..." | tee -a $LOG_FILE
  docker stop "$SERVICE" 2>/dev/null || true
  docker rm "$SERVICE" 2>/dev/null || true

  echo "🔓 Releasing port $PORT if in use..." | tee -a $LOG_FILE
  sudo fuser -k "$PORT"/tcp 2>/dev/null || true

  echo "📦 Checking if image $IMAGE_TAG exists locally..." | tee -a $LOG_FILE
  if docker image inspect "$IMAGE_TAG" > /dev/null 2>&1; then
    echo "✅ Image found locally. Skipping pull." | tee -a $LOG_FILE
  else
    echo "📥 Pulling $IMAGE_TAG..." | tee -a $LOG_FILE
    docker pull "$IMAGE_TAG" | tee -a $LOG_FILE
  fi

  echo "🚀 Starting container with memory limits..." | tee -a $LOG_FILE
  docker run -d \
    --memory="300m" \
    --memory-swap="512m" \
    --name "$SERVICE" \
    -p "$PORT:$PORT" \
    "$IMAGE_TAG" | tee -a $LOG_FILE

  echo "✅ $SERVICE deployed and listening on port $PORT" | tee -a $LOG_FILE
done

echo -e "\n🧹 Pruning unused Docker images..." | tee -a $LOG_FILE
docker image prune -f > /dev/null

echo -e "\n📦 Running containers:" | tee -a $LOG_FILE
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}" | tee -a $LOG_FILE

echo -e "\n✅ All services deployed successfully at $(date)\n" | tee -a $LOG_FILE
