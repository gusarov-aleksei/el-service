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
  type = string
}

variable "el_service_docker_image" {
  description = "El service docker image placed in Artifactory Registry to deploy"
  type = string
}

variable "el_service_bucket_name" {
  description = "Bucket with main el-service data where pdf file are stored"
  type = string
  # default = "algus-bucket"
}