# declare google provider usage for communications with GCP
provider "google" {
  credentials = file(var.credentials)

  project = var.project_id
  region  = var.region
  zone    = var.zone
}

# firewall rules for el-service instances network traffic
resource "google_compute_firewall" "el_rule" {
  name        = "el-service-traffic"
  network     = "default"
  description = "Creates firewall rule targeting el-service instances"

  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }
  target_tags   = ["el-service-tag"]
  source_ranges = ["0.0.0.0/0"]
  direction     = "INGRESS"
}

# this module allows to create instance with container optimized OS
# (like gcloud compute instance-templates create-with-container)
module "gce-container" {
  # https://github.com/terraform-google-modules/terraform-google-container-vm
  source  = "terraform-google-modules/container-vm/google"
  version = "~> 2.0"

  container = {
    image = var.el_service_docker_image
    env = [
      {
        name  = "STORAGE_TYPE"
        value = "GS"
      }
    ]
    name = "el-service-vm-template"
  }
  restart_policy = "Always"
}

# instance template with docker container
resource "google_compute_instance_template" "el_template" {
  name         = "el-service-vm-template"
  machine_type = "e2-micro"

  tags = ["el-service-tag"]

  metadata = { "gce-container-declaration" = module.gce-container.metadata_value }

  disk {
    source_image = module.gce-container.source_image
  }

  network_interface {
    network = "default"

    access_config {}
  }

  service_account {
    email  = "algus-el-service@algus-project-382.iam.gserviceaccount.com"
    scopes = ["cloud-platform"]
  }
}

# managed instance group
resource "google_compute_instance_group_manager" "el_service_group" {
  name        = "el-service-vm-group"
  description = "El-service instance group"

  base_instance_name = "el-service-vm-instance"

  version {
    instance_template = google_compute_instance_template.el_template.id
  }

  target_size = 1
}