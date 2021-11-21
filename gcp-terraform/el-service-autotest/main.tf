# declare google provider usage for communications with GCP
provider "google" {
  credentials = file(var.credentials)

  project = var.project_id
  region  = var.region
  zone    = var.zone
}
# https://www.terraform.io/docs/language/functions/format.html
locals {
  arch_name     = "autotests.zip"
  archive_folder = "../"
  archive_file   = format("%s%s", local.archive_folder, local.arch_name)
}

output "path_to_autotests_archive" {
  description = "Relative path to autotests archive"
  value       = abspath(local.archive_file)
}

# https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/cloudfunctions_function
# https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/storage_bucket
# Create bucket for autotests source code
resource "google_storage_bucket" "el_service_autotests" {
  name          = "el-service-autotests"
  location      = "US"
  force_destroy = true

  uniform_bucket_level_access = true

  lifecycle_rule {
    condition {
      age = 1
    }
    action {
      type = "Delete"
    }
  }

}

# Create object with autotest source code archive
resource "google_storage_bucket_object" "archive" {
  name   = local.arch_name
  bucket = google_storage_bucket.el_service_autotests.name
  source = local.arch_name

  depends_on = [
    null_resource.pack_autotests,
  ]
}

# https://registry.terraform.io/providers/hashicorp/null/latest/docs/resources/resource
# https://www.terraform.io/docs/language/resources/provisioners/local-exec.html
resource "null_resource" "pack_autotests" {

  provisioner "local-exec" {
    command = join(" && ", [
      "cd ./../../autotest/",
      "zip -r autotests.zip * -x *.idea* *.sh *__pycache__* README.md",
      "mv autotests.zip ./../gcp-terraform/el-service-autotest"
    ])

  }
}

/*data "archive_file" "autotest_arch" {
  depends_on  = [null_resource.pack_autotests]

  output_path = "./au.zip"
  source_dir  = "./temp"
  type        = "zip"
} */
