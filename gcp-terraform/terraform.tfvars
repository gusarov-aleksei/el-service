# overall GCP project inputs:
credentials = "../env/gcp/algus-project-382-4c2c24b9861e.json"
project_id  = "algus-project-382"
region      = "us-east1"
zone        = "us-east1-b"
# module.el-service inputs:
el_service_docker_image = "us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1"
el_service_bucket_name  = "algus-bucket"

# module.el-service-autotests inputs:
au_function_name  = "el_service_au_function"
au_function_topic = "el-service-autotest-topic"