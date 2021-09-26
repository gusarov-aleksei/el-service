#!/bin/bash

# stop script in case of command error
set -eou pipefail

echo "*** Set project properties"
# set default compute/region and compute/zone
gcloud config set compute/region us-east1
gcloud config set compute/zone us-east1-b

# create VM with container and pass environment variable
echo "*** Create resources of compute"
gcloud compute instances create-with-container el-service-vm --machine-type e2-micro --container-image us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1 --container-env STORAGE_TYPE=GS --service-account algus-el-service@algus-project-382.iam.gserviceaccount.com --scopes=cloud-platform

# add tag to el-service-vm
gcloud compute instances add-tags el-service-vm --tags el-service-tag

# open port and and connect it to el-service-vm
echo "*** Create resources of network"
gcloud compute firewall-rules create el-service-traffic --direction=INGRESS --priority=1000 --network=default --action=ALLOW --rules=tcp:8080 --source-ranges=0.0.0.0/0 --target-tags=el-service-tag
