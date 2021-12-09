locals {
  au_source_dir = "${path.root}/${var.au_source_relative_path}"
  au_arch_name = "au.zip"
  au_trigger_topic_full_path = "projects/${var.project_id}/topics/${var.au_function_topic}"
}

data "archive_file" "au_source_zip" {
  type        = "zip"
  output_path = "${path.module}/${local.au_arch_name}"
  source_dir = local.au_source_dir
  excludes = [".idea", "__pycache__", "README.md","*.sh", "data"]
}
# TODO add calculation to au.zip file name

# another approach of packing
/*
resource "null_resource" "pack_autotests" {
  provisioner "local-exec" {
    command = join(" && ", [
      "cd ./../autotest/",
      "zip -r autotests.zip * -x *.idea* *.sh *__pycache__* README.md",
      "mv autotests.zip ./../gcp-terraform/el-service-autotest"
    ])

  }
}
*/

resource "google_storage_bucket" "fn_source_code" {
  name          = "el-service-functions-source"
  location      = var.region
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
resource "google_storage_bucket_object" "au_source_blob" {
  name   = local.au_arch_name
  bucket = google_storage_bucket.fn_source_code.name
  source = data.archive_file.au_source_zip.output_path


  depends_on = [
    data.archive_file.au_source_zip
  ]
}

# https://cloud.google.com/functions/docs/
resource "google_cloudfunctions_function" "function" {
  name        = var.au_function_name
  description = "El Service autotests"
  runtime     = "python39"

  available_memory_mb   = 128
  source_archive_bucket = google_storage_bucket.fn_source_code.name
  source_archive_object = google_storage_bucket_object.au_source_blob.name
  timeout = 60

  event_trigger {
    event_type = "google.pubsub.topic.publish"
    resource   = local.au_trigger_topic_full_path
  }
  # keep for example
  labels = {
    my-label = "my-label-value"
  }

  environment_variables = {
    MY_ENV_VAR = "my-env-var-value"
  }
}

resource "google_pubsub_topic" "au_topic" {
  name = var.au_function_topic
}

resource "google_pubsub_subscription" "au_sub" {
  name    = "el-service-au-sub"
  topic   = google_pubsub_topic.au_topic.name
}

# IAM entry for all users to invoke the function
resource "google_cloudfunctions_function_iam_member" "invoker" {
  project        = google_cloudfunctions_function.function.project
  region         = google_cloudfunctions_function.function.region
  cloud_function = google_cloudfunctions_function.function.name

  role   = "roles/cloudfunctions.invoker"
  # set exact principalEmail, like user account 'user:login@gmail.com' or service account
  member = "allUsers"
}