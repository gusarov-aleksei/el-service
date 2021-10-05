#!/bin/bash

# clean group
echo "*** Delete resources of compute engine"
gcloud compute instance-groups managed delete el-service-vm-group

# clean templates
gcloud compute instance-templates delete el-service-vm-template

# clean heath-checks
gcloud compute health-checks delete el-service-health-check

echo "*** Delete resources of network"
gcloud compute firewall-rules delete el-service-traffic
