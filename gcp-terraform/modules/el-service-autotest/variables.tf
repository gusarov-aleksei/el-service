variable "project_id" {
  description = "A project ID to deploy resource into"
  type        = string
}

variable "region" {
  description = "A region to deploy instances into"
  type        = string
}

variable "au_source_relative_path" {
  description = "Autotests source code relative path (root directory is 'gcp-terraform')"
  type        = string
}

variable "au_function_name" {
  description = "Name of entry point Cloud Function (this function is called by Cloud Function trigger)"
  type        = string
}

variable "au_function_topic" {
  description = "Name of Pub/Sub topic consumed by trigger of Cloud Function"
  type        = string
}