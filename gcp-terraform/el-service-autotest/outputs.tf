output "path_to_autotests_archive" {
  description = "Absolute path to autotests archive"
  value       = abspath(local.archive_file)
}