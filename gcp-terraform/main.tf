# declare google provider usage for communications with GCP
provider "google" {
  credentials = file(var.credentials)

  project = var.project_id
  region  = var.region
  zone    = var.zone
}

module "el_service_au_static" {
  source = "./modules/el-service-au-static"
  au_source_relative_path = var.au_source_relative_path
  region = var.region

  count = 1
}

module "el_service_au" {
  source = "./modules/el-service-autotest"

  region            = var.region
  project_id        = var.project_id
  au_function_name  = var.au_function_name
  au_function_topic = var.au_function_topic
  au_source_relative_path = var.au_source_relative_path
}

module "el_service" {
  source = "./modules/el-service"

  el_service_docker_image = var.el_service_docker_image
  project_id              = var.project_id
  region                  = var.region
  zone                    = var.zone
  el_service_bucket_name  = var.el_service_bucket_name
}