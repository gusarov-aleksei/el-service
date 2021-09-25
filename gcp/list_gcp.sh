#!/bin/bash

# list all using resources
echo "*** Project properties"
gcloud config get-value compute/region
gcloud config get-value compute/zone

echo "*** Show Compute resources"
gcloud compute instances list

echo "*** Show Network resources"
gcloud compute firewall-rules describe el-service-traffic

echo "*** Show Storage resources"
gsutil ls -l gs://algus-bucket/**

echo "*** Show docker images"
gcloud artifacts docker images list us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo

