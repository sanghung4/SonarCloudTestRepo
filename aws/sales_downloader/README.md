# Sales Data Downloader

## Building
From the root of the project
```shell
$ pwd
/Users/kahlil/Documents/innovation-post
$ docker build -t sales_downloader aws/sales_downloader/.
```

## Pushing
Log in to ECR
```shell
aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin 773663557741.dkr.ecr.us-east-1.amazonaws.com
```
> Build using the build command if you don't have a current build

Tag the container
```shell
docker tag dev-sales-downloader:latest 773663557741.dkr.ecr.us-east-1.amazonaws.com/dev-sales-downloader:latest
```

Pushing the container
```shell
docker push 773663557741.dkr.ecr.us-east-1.amazonaws.com/dev-sales-downloader:latest
```