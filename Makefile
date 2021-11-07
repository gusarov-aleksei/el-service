build:
	mvn clean install
	docker build -t el-service:0.1 .
build-docker:
	docker build -t el-service:0.1 .
run:
	docker run -i --rm -p 8080:8080 -p 5007:5007 el-service:0.1
build-gcp:
	gcloud builds submit
tf-apply:
	cd gcp-terraform; terraform apply;
tf-destroy:
	cd gcp-terraform; terraform destroy;
tf-out:
	cd gcp-terraform; terraform output;
tests:
	cd autotest && python tests.py