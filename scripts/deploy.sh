#!/bin/bash

set -e  # Stop script on error

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

echo "🚀 Starting clean microservice deployment..."

for SERVICE in "${SERVICES[@]}"; do
  PORT=$(get_port "$SERVICE")
  echo ""
  echo "🔄 Deploying $SERVICE on port $PORT"

  echo "🛑 Stopping container if running..."
  docker stop "$SERVICE" 2>/dev/null || true

  echo "🗑️ Removing old container..."
  docker rm "$SERVICE" 2>/dev/null || true

  echo "🧼 Removing old images of $SERVICE..."
  docker rmi $(docker images "$DOCKER_USER/$SERVICE" -q) 2>/dev/null || true

  echo "🔓 Releasing port $PORT if blocked..."
  fuser -k "$PORT"/tcp 2>/dev/null || true

  echo "📥 Pulling latest image..."
  docker pull "$DOCKER_USER/$SERVICE:latest"

  echo "🚀 Starting new container..."
  docker run -d --name "$SERVICE" -p "$PORT:$PORT" "$DOCKER_USER/$SERVICE:latest"

  echo "✅ $SERVICE is now running on port $PORT"
done

echo ""
echo "🎉 All services have been deployed successfully!"
