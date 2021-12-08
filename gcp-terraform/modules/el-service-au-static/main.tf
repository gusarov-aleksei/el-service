locals {
  au_source_dir = "${path.root}/${var.au_source_relative_path}"
  file1 = "data/file one.pdf"
  file2 = "data/file two.pdf"
  au_path_file1 = "${local.au_source_dir}/${local.file1}"
  au_path_file2 = "${local.au_source_dir}/${local.file2}"
  au_static_data_bucket_name = "au-static-data"
}

resource "google_storage_bucket" "au_static_data" {
  name                        = "el-service-au-static"
  location                    = var.region
  force_destroy               = false

  uniform_bucket_level_access = true

  lifecycle {
    prevent_destroy = true
  }

}

# if au_file1 exists don't upload it

# Create objects with test data files
resource "google_storage_bucket_object" "au_file1" {
  name   = local.file1
  bucket = google_storage_bucket.au_static_data.name
  source = local.au_path_file1
}

resource "google_storage_bucket_object" "au_file2" {
  name   = local.file2
  bucket = google_storage_bucket.au_static_data.name
  source = local.au_path_file2
}
