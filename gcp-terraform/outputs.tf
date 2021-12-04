output "el_service_module_output" {
  value = module.el_service.el_service_module_output
}

output "el_service_image" {
  value = module.el_service.gcr_el_service_image
}

output "el_service_instances" {
  value = module.el_service.instances
}

output "el_service_instance_ip_addresses" {
  value = module.el_service.instance_public_ip_addresses
}

output "au_source_dir_rel_output" {
  value = module.el_service_au.au_source_dir_rel_output
}

output "au_source_dir_abs_output" {
  value = module.el_service_au.au_source_dir_abs_output
}

output "au_module_output" {
  value = module.el_service_au.au_module_output
}

output "tf_root_path" {
  description = "Terraform folder absolut path"
  value       = path.cwd
}

output "au_source_blob_name_output" {
  value = module.el_service_au.au_source_blob_name_output
}