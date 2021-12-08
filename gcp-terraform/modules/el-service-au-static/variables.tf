variable "region" {
  description = "A region to deploy instances into"
  type        = string
}

variable "au_source_relative_path" {
  description = "Autotests source code relative path (root directory is 'gcp-terraform')"
  type        = string
}