build:
	mvn clean install
	docker build -t el-service:0.1 .
build-docker:
	docker build -t el-service:0.1 .
run:
	docker run -i --rm -p 8080:8080 -p 5007:5007 el-service:0.1
build-gcp:
	gcloud builds submit
