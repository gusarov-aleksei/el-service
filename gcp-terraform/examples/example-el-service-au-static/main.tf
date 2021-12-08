# declare google provider usage for communications with GCP
provider "google" {
  credentials = file(var.credentials)

  project = var.project_id
  region  = var.region
  zone    = var.zone
}

module "el_service_au_static_data" {
  source                  = "../../modules/el-service-au-static"
  au_source_relative_path = var.au_source_relative_path
  region                  = var.region

  count = 1
}