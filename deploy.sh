#!/bin/bash

# Set your DockerHub image name
IMAGE_NAME="ajeetkumar238199/userservice"

# Use passed commit message or fallback
COMMIT_MESSAGE=${1:-"Auto-deploy update"}

echo "🔧 STEP 1: Building Docker image..."
docker build -t $IMAGE_NAME .

if [ $? -ne 0 ]; then
  echo "❌ Docker build failed."
  exit 1
fi

echo "📤 STEP 2: Pushing Docker image to DockerHub..."
docker push $IMAGE_NAME:latest

if [ $? -ne 0 ]; then
  echo "❌ Docker push failed."
  exit 1
fi

echo "📁 STEP 3: Committing local code to GitHub..."
git add .
git commit -m "$COMMIT_MESSAGE"
git push origin master

if [ $? -ne 0 ]; then
  echo "❌ Git push failed."
  exit 1
fi

echo "✅ DONE: CI/CD pipeline triggered. Check GitHub Actions for deployment status."
