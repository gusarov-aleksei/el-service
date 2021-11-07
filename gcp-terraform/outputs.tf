data "google_container_registry_image" "el_service_image" {
  name = "us-east1-docker.pkg.dev/algus-project-382/algus-docker-repo/el-service:0.1"
}

output "gcr_el_service_image" {
  description = "Reference to el-service Docker image"
  value = data.google_container_registry_image.el_service_image.image_url
}

data "google_compute_instance_group" "el_service_vm_group" {
  name = "el-service-vm-group"
  zone = var.zone
}

output "instances" {
  description = "A link of the deployed instance"
  value       = data.google_compute_instance_group.el_service_vm_group.instances
}

data "google_compute_instance" "el_service_vm_instances" {
  for_each = data.google_compute_instance_group.el_service_vm_group.instances
  self_link = each.key
  zone = var.zone
}

output "instance_public_ip_addresses" {
  description = "Public IP address of the deployed vm instances"
  value = {
      for instance in data.google_compute_instance.el_service_vm_instances:
      instance.name => instance.network_interface.0.access_config.0.nat_ip
  }
}