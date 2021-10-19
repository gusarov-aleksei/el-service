import os
import subprocess
import googleapiclient
import googleapiclient.discovery


# TODO extend filtration with tags
# list all compute instances in project and zone
def list_instances(compute, project, zone):
    result = compute.instances().list(project=project, zone=zone).execute()
    return result['items'] if 'items' in result else None


# GOOGLE_APPLICATION_CREDENTIALS enf variable is mandatory to perform this function
def get_gcp_compute_resource():
    return googleapiclient.discovery.build('compute', 'v1')


# get simply first instance and first network interface from it
# assume simple configuration at present moment
def get_external_ip(compute):
    compute_instances = list_instances(compute, "algus-project-382", "us-east1-b")
    return compute_instances[0]['networkInterfaces'][0]['accessConfigs'][0]['natIP']
