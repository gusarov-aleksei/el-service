terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "4.0.0"
    }
  }
}

provider "google" {
  credentials = file("../env/gcp/algus-project-382-4c2c24b9861e.json")

  project = "algus-project-382"
  region  = "us-east1"
  zone    = "us-east1-b"
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

data "google_container_registry_image" "el_service_image" {
  name = "us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1"
}

output "gcr_location" {
  value = data.google_container_registry_image.el_service_image.image_url
}

# this module allows to create instance with container optimized OS
# (like gcloud compute instance-templates create-with-container)
module "gce-container" {
  # https://github.com/terraform-google-modules/terraform-google-container-vm
  source  = "terraform-google-modules/container-vm/google"
  version = "~> 2.0"

  container = {
    # image="us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service@sha256:8234d3c652795c75ee47ea5573d5c93381dcb4a8af3e53ea449ec41e178717ff"
    # image = "gcr.io/us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1"
    image = "us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1"
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