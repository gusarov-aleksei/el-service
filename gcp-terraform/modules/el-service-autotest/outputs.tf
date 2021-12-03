output "au_source_dir_rel_output" {
  description = "Relative Path to autotest folder from TF root directory"
  value = local.au_source_dir
}

output "au_source_dir_abs_output" {
  description = "Relative Path to autotest folder from TF root directory"
  value = abspath(local.au_source_dir)
}

output "au_module_output" {
  description = "Output module path"
  value = path.module
}

output "au_source_blob_name_output" {
  description = "Output autotests source code in bucket"
  value = google_storage_bucket_object.au_source_blob.output_name
}

output "au_source_code_hash" {
  description = "Value of au.zip archive MD5 calculation"
  value = data.archive_file.au_source_zip.output_md5
}