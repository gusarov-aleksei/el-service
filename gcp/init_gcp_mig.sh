#!/bin/bash

# stop script in case of command error
set -eou pipefail

echo "*** Set project properties"
# set default compute/region and compute/zone
gcloud config set compute/region us-east1
gcloud config set compute/zone us-east1-b

# open port for el-service-vm
echo "*** Create resources of network"
gcloud compute firewall-rules create el-service-traffic --direction=INGRESS --priority=1000 --network=default --action=ALLOW --rules=tcp:8080 --source-ranges=0.0.0.0/0 --target-tags=el-service-tag

# create instance template, managed instance group
echo "*** Create resources of compute engine"
# create instance template
echo "*** Create instance templates"
gcloud compute instance-templates create-with-container el-service-vm-template --machine-type e2-micro --container-image us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1 --container-env STORAGE_TYPE=GS --service-account algus-el-service@algus-project-382.iam.gserviceaccount.com --scopes=cloud-platform --tags el-service-tag

#echo "*** Create health-checks"
gcloud compute health-checks create http el-service-health-check --project=algus-project-382 --port=8080 --request-path=/q/health/live --proxy-header=NONE --no-enable-logging --check-interval=5 --timeout=5 --unhealthy-threshold=2 --healthy-threshold=2

echo "*** Create instance group"
gcloud compute instance-groups managed create el-service-vm-group --size 1 --template el-service-vm-template --base-instance-name el-service-vm-instance

#echo "*** Attach health-check to instance group"
gcloud compute instance-groups managed update el-service-vm-group --health-check el-service-health-check --initial-delay 180