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
  type = string
}