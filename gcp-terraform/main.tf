# declare google provider usage for communications with GCP
provider "google" {
  credentials = file(var.credentials)

  project = var.project_id
  region  = var.region
  zone    = var.zone
}

module "el_service" {
  source = "./el-service"

  project_id = var.project_id
  region     = var.region
  zone       = var.zone

  el_service_docker_image = var.el_service_docker_image

}

module "el_service_au" {
  source = "./el-service-autotest"

  au_bucket_location              = var.au_bucket_location
  au_function_name                = var.au_function_name
  au_function_topic               = var.au_function_topic
  au_function_topic_resource_path = var.au_function_topic_resource_path
  credentials                     = var.credentials
  project_id                      = var.project_id
  region                          = var.region
  zone                            = var.zone
}