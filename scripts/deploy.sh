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
    postapi) echo 8085 ;;
    apigateway) echo 9090 ;;
    *) echo "Unknown service: $1" && exit 1 ;;
  esac
}

echo "🚀 Starting microservice deployment..."

for SERVICE in "${SERVICES[@]}"; do
  PORT=$(get_port "$SERVICE")
  IMAGE_TAG="$DOCKER_USER/$SERVICE:latest"

  echo ""
  echo "🔄 Deploying $SERVICE on port $PORT"

  echo "🛑 Stopping and removing existing container (if any)..."
  docker stop "$SERVICE" 2>/dev/null || true
  docker rm "$SERVICE" 2>/dev/null || true

  echo "🔓 Releasing port $PORT if in use..."
  fuser -k "$PORT"/tcp 2>/dev/null || true

  echo "📦 Checking if image $IMAGE_TAG already exists locally..."
  if docker image inspect "$IMAGE_TAG" > /dev/null 2>&1; then
    echo "✅ Image found locally. Skipping pull."
  else
    echo "📥 Image not found. Pulling $IMAGE_TAG..."
    docker pull "$IMAGE_TAG"
  fi

  echo "🚀 Starting container..."
  CONTAINER_ID=$(docker run -d --name "$SERVICE" -p "$PORT:$PORT" "$IMAGE_TAG")
  echo "🆔 $SERVICE container started with ID: $CONTAINER_ID"
done

echo ""
echo "🧹 Cleaning up unused Docker images..."
docker image prune -f > /dev/null

echo ""
echo "📦 Currently running containers:"
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}"

echo ""
echo "🎉 All services deployed successfully!"
