#!/bin/bash

set -e

DOCKER_USER=ajeetkumar238199
LOG_FILE="/home/ubuntu/deploy.log"

SERVICES_STAGE1=("serviceregistry" "apigateway" "userservice")
SERVICES_STAGE2=("filestorage" "filemetadata" "giftservice" "postapi")

get_port() {
  case "$1" in
    serviceregistry) echo 8761 ;;
    userservice) echo 8081 ;;
    filestorage) echo 8082 ;;
    filemetadata) echo 8088 ;;
    giftservice) echo 8084 ;;
    postapi) echo 8087 ;;
    apigateway) echo 9090 ;;
    *) echo "Unknown service: $1" && exit 1 ;;
  esac
}

deploy_service() {
  SERVICE=$1
  PORT=$(get_port "$SERVICE")
  IMAGE_TAG="$DOCKER_USER/$SERVICE:latest"

  echo -e "\n🔄 Deploying $SERVICE on port $PORT" | tee -a $LOG_FILE

  echo "🛑 Stopping and removing existing container (30s timeout)..." | tee -a $LOG_FILE
  docker stop -t 30 "$SERVICE" 2>/dev/null || true
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

  # Per-service JVM tuning
  if [[ "$SERVICE" == "postapi" ]]; then
    JVM_OPTS="-Xms48m -Xmx192m"
    echo "⏳ Waiting a bit more before launching postapi..." | tee -a $LOG_FILE
    sleep 8
  elif [[ "$SERVICE" == "serviceregistry" ]]; then
    JVM_OPTS="-Xms32m -Xmx160m"
    echo "⏳ Giving extra time for Eureka registry startup..." | tee -a $LOG_FILE
    sleep 5
  else
    JVM_OPTS="-Xms64m -Xmx256m"
  fi

  echo "🚀 Starting container with memory limits and JVM tuning..." | tee -a $LOG_FILE
  docker run -d \
    --memory="300m" \
    --memory-swap="512m" \
    --name "$SERVICE" \
    -e JAVA_OPTS="$JVM_OPTS" \
    -p "$PORT:$PORT" \
    "$IMAGE_TAG" >> $LOG_FILE

  # Sleep longer after run per service
  if [[ "$SERVICE" == "serviceregistry" ]]; then
    sleep 20
  elif [[ "$SERVICE" == "postapi" ]]; then
    sleep 15
  else
    sleep 10
  fi

  echo "📝 First 10 log lines from $SERVICE:" | tee -a $LOG_FILE
  docker logs "$SERVICE" 2>&1 | head -n 10 >> $LOG_FILE

  if [ $? -ne 0 ]; then
    echo "❌ $SERVICE failed to start. Showing last logs:" | tee -a $LOG_FILE
    docker logs "$SERVICE" 2>&1 | tail -n 20 | tee -a $LOG_FILE
  else
    echo "✅ $SERVICE deployed and listening on port $PORT" | tee -a $LOG_FILE
  fi
}

# 🚀 Start deployment
echo -e "\n🚀 Deploy started at $(date)\n" | tee -a $LOG_FILE

echo "📦 Deploying core services (Stage 1)..." | tee -a $LOG_FILE
for SERVICE in "${SERVICES_STAGE1[@]}"; do
  deploy_service "$SERVICE"
done

echo -e "\n⏳ Waiting before Stage 2 deployment..." | tee -a $LOG_FILE
sleep 10

echo "📦 Deploying remaining services (Stage 2)..." | tee -a $LOG_FILE
for SERVICE in "${SERVICES_STAGE2[@]}"; do
  deploy_service "$SERVICE"
done

echo -e "\n🧹 Pruning unused Docker images..." | tee -a $LOG_FILE
docker image prune -f > /dev/null

echo -e "\n📦 Running containers:" | tee -a $LOG_FILE
docker ps --format "table {{.Names}}\t{{.Image}}\t{{.Status}}\t{{.Ports}}" | tee -a $LOG_FILE

echo -e "\n✅ All services deployed successfully at $(date)\n" | tee -a $LOG_FILE
