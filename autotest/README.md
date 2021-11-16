# Autotests

This is folder with solution tests, tests of El Service. They validate result of El Service output through request/response of REST endpoint.

### Implementation details

Standard library of Python 3.9.9 is used for scripts overall. Also following libraries could be distinguished for specific goals:
- standard [unittest](https://docs.python.org/3/library/unittest.html) library is used as unit-test framework
- [requests](https://docs.python-requests.org/en/latest/) (of 2.26.0 version) is chosen for making HTTP-calls (library installation is required)
- [google-api-python-client](https://pypi.org/project/google-api-python-client/) (of 2.27.0 version) to automate some operations with GCP, e.g., getting IP address of deployed service (see details in official [documentation](https://cloud.google.com/apis/docs/client-libraries-explained) and [source repository](https://github.com/googleapis/google-api-python-client/blob/main/docs/README.md)). [Authentication](#authentication) to GCP is required.

Library version is marked for consistency, for moving to new version some compatibility checks may be required.
All tests are supposed to place in [tests](./tests) folder. 

### Authentication

To be able to use GCP API as a client, authentication is required. For this purpose following steps to be done as described in [document](https://cloud.google.com/docs/authentication/getting-started):
1. create Service Account 
2. create Service Account Key
3. download Key (it would be a file named `algus-project-382-4c2c24b9861e.json`)
4. create environment var **GOOGLE_APPLICATION_CREDENTIALS** with path to downloaded key `algus-project-382-4c2c24b9861e.json`

### Commands to run

That tests can be run locally at dev workstation or can be run remotely at GCP environment.
- to run autotests at localhost when el-service is up at localhost:
```
python tests.py 
```
or explicitly pass **all_local** value of **deploy_type** argument:
```
python tests.py --deploy_type all_local
```
- to run autotests at localhost when el-service is up at GCP, need to execute script with **cloud** value for **deploy_type**:
```
python tests.py --deploy_type cloud
```
- to run autotests deployed in GCP (el-service is at GCP also), need to pass **all_cloud** value for **deploy_type**:
```
python tests.py --deploy_type all_cloud
```
