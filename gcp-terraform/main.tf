terraform {
  required_providers {
    google = {
      source  = "hashicorp/google"
      version = "3.5.0"
    }
  }
}

provider "google" {
  credentials = file("../env/gcp/algus-project-382-4c2c24b9861e.json")

  project = "algus-project-382"
  region  = "us-east1"
  zone    = "us-east1-b"
}

resource "google_compute_network" "el_network" {
  name = "el-service-network"
}

resource "google_compute_firewall" "el_rule" {
  name = "el-service-traffic"
  network = google_compute_network.el_network.name
  description = "Creates firewall rule targeting el-service instances"

  allow {
    protocol = "tcp"
    ports    = ["8080"]
  }
  target_tags   = ["el-service-tag"]
  source_ranges = ["0.0.0.0/0"]
  direction   = "INGRESS"
}

resource "google_compute_instance" "vm_instance" {
  name         = "terraform-instance"
  machine_type = "f1-micro"
  tags         = ["web", "dev"]

  boot_disk {
    initialize_params {
      image = "cos-cloud/cos-stable"
    }
  }

  network_interface {
    network = google_compute_network.el_network.name
    access_config {
    }
  }
}