#!/bin/bash

# Default values
USERNAME="admin"
PASSWORD="password"

# Named arguments parsing
for arg in "$@"; do
  case $arg in
    --username=*)
      USERNAME="${arg#*=}"
      shift
      ;;
    --password=*)
      PASSWORD="${arg#*=}"
      shift
      ;;
    *)
      echo "!!! Unknown argument: $arg"
      ;;
  esac
done

IMAGE_NAME="fx-comparator"
CONTAINER_NAME="fx-comparator"

echo ">>> Building application with Maven Wrapper..."
./mvnw clean package -DskipTests

if [ $? -ne 0 ]; then
  echo "XXX Build failed."
  exit 1
fi

echo "OK - Build success."

echo ">>> Building Docker image: $IMAGE_NAME"
docker build --platform linux/amd64 -t $IMAGE_NAME .

echo ">>> Cleaning existing container (if exists)..."
docker rm -f $CONTAINER_NAME 2>/dev/null

echo ">>> Running Docker container with username='$USERNAME' and password='$PASSWORD'"
docker run --platform linux/amd64 \
  -p 8080:8080 \
  -p 8088:8088 \
  -e "USERNAME=$USERNAME" \
  -e "PASSWORD=$PASSWORD" \
  --name $CONTAINER_NAME \
  $IMAGE_NAME
