variable "credentials" {
  description = "A path to file with credentials for service account"
  type        = string
}

variable "project_id" {
  description = "A project ID to deploy resource into"
  type        = string
}

variable "zone" {
  description = "A zone to deploy instances into"
  type        = string
}

variable "region" {
  description = "A region to deploy instances into"
  type        = string
}

variable "el_service_docker_image" {
  description = "El service docker image placed in Artifactory Registry to deploy"
  type        = string
}

variable "au_function_name" {
  description = "Name of entry point Cloud Function (this function is call by Cloud Function trigger)"
  type        = string
}

variable "au_function_topic" {
  description = "Name of Pub/Sub topic consumed by trigger of Cloud Function"
  type        = string
}

variable "el_service_bucket_name" {
  description = "Bucket with main el-service data where pdf file are stored"
  type = string
}