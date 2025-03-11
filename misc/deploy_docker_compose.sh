#!/bin/bash
set -e
cd ~/schreib-mate-bot
echo "Pulling latest changes from Git..."
git pull
echo "Starting containers with the latest changes..."
docker-compose up --build --pull always -d
echo "Cleanup: Removing unused Docker resources..."
docker system prune -f