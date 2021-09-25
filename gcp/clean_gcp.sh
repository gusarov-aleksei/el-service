#!/bin/bash
# clean GCP  resources

# stop script in case of command error
#set -eou pipefail

echo "*** Delete resources of network"
gcloud compute firewall-rules delete el-service-traffic

echo "*** Delete resources of compute engine"
gcloud compute instances delete el-service-vm

echo "*** Delete resources of storage"
gsutil rm -r gs://algus-bucket/**