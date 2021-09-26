#!/bin/bash

# stop script in case of command error
set -eou pipefail

echo "*** Create new service account"
gcloud iam service-accounts create algus-el-service --description "Account for file processing" --display-name="File processor instance"

echo "*** Add policies to the service account"
gcloud projects add-iam-policy-binding algus-project-382 --member=serviceAccount:algus-el-service@algus-project-382.iam.gserviceaccount.com --role=roles/storage.objectAdmin
gcloud projects add-iam-policy-binding algus-project-382 --member=serviceAccount:algus-el-service@algus-project-382.iam.gserviceaccount.com --role=roles/artifactregistry.reader