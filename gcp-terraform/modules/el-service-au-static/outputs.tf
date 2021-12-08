output "au_static_file1" {
  description = "Check file1 remotely"
  value = google_storage_bucket_object.au_file1.output_name
}

output "au_static_file2" {
  description = "Check file2 remotely"
  value = google_storage_bucket_object.au_file2.output_name
}