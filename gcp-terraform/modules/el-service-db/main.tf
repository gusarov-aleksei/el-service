resource "google_sql_database_instance" "master" {
  name             = "master-instance"
  database_version = "POSTGRES_13"
  region           = "us-east1"

  settings {
    # Second-generation instance tiers are based on the machine
    # type. See argument reference below.
    tier = "db-f1-micro"
  }
}