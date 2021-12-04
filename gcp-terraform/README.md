# Provisioning with Terraform

Here is description of how GCP resources are provisioned using IaaC approach by means of [Terraform](https://www.terraform.io/).
This tool allows configuring interactions with many cloud APIs(of different popular providers) in declarative way.

### Modules for structure

Solution configuration can be organized as separated modules for better maintenance and usability:
- [el-service](./modules/el-service) - 'el-service' application resources with [Managed instance group](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/compute_instance_group_manager) as a key resource
- [el-service-autotest](./modules/el-service-autotest) - autotests for 'el-service' with [Cloud function](https://registry.terraform.io/providers/hashicorp/google/latest/docs/resources/cloudfunctions_function)

Resources of each module can be managed by TF independently.

### Commands to operate

Terraform will ask to enter 'init' command at first time of terraform using, or when new provider is involved into configuration (for example, provider "google"), or in case of module just added to configuration 
```
terraform init
```
First step is to create configuration. 
Config files can be formatted (change content positioning in accordance with TF style visualising):
```
terraform fmt
```
Good practice is to validate config if it has no syntax or logic errors
```
terraform validate
```
and to inspect execution plan comparing current configuration with prior state showing difference
```
terraform plan
```
Apply and destroy configuration:
```
terraform apply
terraform destroy
```
Show current state of applied configuration:
```
terraform show
```
Print outputs of TF configuration (for convenience outputs are gathered in [outptus.tf](./outputs.tf))
```
terraform output
```
Refresh state from all managed remote objects(useful for output values calculated after applying configuration)
```
terraform refresh
```
TF suggests variety of convenient command arguments to be more flexible in resource management.
For instance, 'target' argument is utilized to affect resources defined in specific module only:
```
terraform apply -target="module.el_service_au"
```
Full documentation can be found [here](https://www.terraform.io/docs/cli/commands)

### A few points mentioned to remember

- pay attention to location of folder with autotests. Path to this folder is defined as relative in TF configuration 'el-service-au'.
- it is highly desirable to exclude static files from source code packed into zip archive in 'el-service-autotest'.
- when autotest resources are being provided, it is possible to send messages to Pub/Sub topic for autotest function triggering:
```
gcloud pubsub topics publish el-service-autotest-topic --message="au-test"
```
- split all resources into modules for possibility of applying each module separately.