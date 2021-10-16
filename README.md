# English learning through programming

### Motivation

Actual project is inspired by English learning process, mainly by English as a Second Language(ESL) Podcast from beautiful Los Angeles, California. 
It feels like intention to embrace culture of Western Civilization, to adopt approaches/practices, and to be aware of programming as result. 
This is about being involved into development processes with possibility to observe artefacts produced by humanity, with possibility to move through present time collecting memories of the past and acquiring dreams of the future.

In terms of programming the past is immutable, it is already established and fixed. The future is mutable - it is not set, yet. Such property of mutability is like property of mind opened for setting goals, goals consistent with environment and society.
"The future is not set. There is no fate but what we make for ourselves", Terminator said in 1991. Remember the message and don't let consciousness become inconsistent.

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
- access HTTP through [EnglishContentResource](./src/main/java/org/example/rest/EnglishContentResource.java), [FileManager](./src/main/java/org/example/rest/FileManagerResource.java)
- ability to deploy into [GCP](https://cloud.google.com/) using Compute Engine, Cloud Storage, Cloud Build, Artifact Registry