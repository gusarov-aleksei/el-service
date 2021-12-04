# English learning through programming

### Motivation

Actual project is inspired by English learning process, mainly by English as a Second Language(ESL) Podcast from beautiful Los Angeles, California. 
It feels like intention to embrace culture of Western Civilization, to adopt approaches/practices, and to be aware of programming as result. 
This is about being involved into development processes with possibility to observe artefacts produced by humanity, with possibility to move through present time collecting memories of the past and acquiring dreams of the future.

In terms of programming the past is immutable, it is already established and fixed. The future is mutable - it is not set, yet. Such property of mutability is like property of mind opened for setting goals, goals consistent with environment and society.
"The future is not set. There is no fate but what we make for ourselves", Terminator said in 1991. Remember the message and don't let sense become inconsistent.

### Introduction

Technically this service is simple version of ETL process. It shows some methods of extracting, transforming, loading:
- text content extracting from pdf (from raw data saved in blob)
- content transformation into some structure (data preparation, cleaning and enrichment if required)
- and loading this structure into storage with text-index support for further processing and analytical methods applying *(this part of solution is to be defined further)*

### Overall description

The service is implemented in Java with [Apache PDFBox](https://pdfbox.apache.org/) for text extraction from pdf and some another third party libraries, with [Quarkus](https://quarkus.io/) libraries for the purpose of being cloud oriented and ready to production.
Here is short description:
- operations on files with [StorageService](./src/main/java/org/example/storage/StorageService.java)
- text extraction from files with [EnglishContentService](./src/main/java/org/example/service/EnglishContentService.java)
- HTTP-access through [EnglishContentResource](./src/main/java/org/example/rest/EnglishContentResource.java), [FileManagerResource](./src/main/java/org/example/rest/FileManagerResource.java)
- ability to deploy into [GCP](https://cloud.google.com/) using Compute Engine, Cloud Storage, Cloud Build, Artifact Registry, Pub/Sub, Cloud Function(used for a [Autotests](#autotests) running)

### How to build and deploy

#### Tools
Working with GCP is possible using different approaches, for example:
- [Scripts](./gcp) of command line interface [gcloud](https://cloud.google.com/sdk/gcloud)
- IaaC with [Terraform](https://www.terraform.io/) approach defined in [gcp-terraform](./gcp-terraform)

#### Building
Here is basic flow of [Docker](https://www.docker.com/) image preparation:
1) Some changes are done in [codebase](./src)
2) Cloud build is performed according to config [cloudbuild.yaml](./cloudbuild.yaml)
with command 'gcloud builds submit' in the same with config directory.
3) As result of step 2) image of built codebase is published in Artifact Registry (here are some [notes](./notes/gcp_docker.txt) of how to work with GCP Docker Registry) 

### Autotests

Any developing system requires its testing as mandatory activity. Examples of autotests are placed in [autotest](./autotest) folder and written using [Python](https://www.python.org/) programming language.
See details in [description](./autotest/README.md).
