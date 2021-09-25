#!/bin/bash

# stop script in case of command error
set -eou pipefail

# build jar
echo "*** Build source code"
mvn clean install

echo "*** Build docker image"
# create docker image
docker build -t el-service:0.1 .
# create docker tag
docker tag el-service:0.1 us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1

echo "*** Upload to GCP artifact registry"
# push to GCP artifactory
docker push us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1

echo "*** Uploading finished"