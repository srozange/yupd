# Yupd
![GitHub release (with filter)](https://img.shields.io/github/v/release/srozange/yupd)
[![Build](https://github.com/srozange/yupd/actions/workflows/maven.yml/badge.svg)](https://github.com/srozange/yupd/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/srozange/yupd/branch/main/badge.svg?token=JCPP4VZ1S1)](https://codecov.io/gh/srozange/yupd)

**Update YAML files the GitOps way**

yupd is a command-line tool that allows updating a YAML file in a remote GitHub or GitLab gitRepository.

## Usage

Assuming we have this file in a Git gitRepository:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx
  labels:
    app: nginx
  annotations:
    last-updated: Sat Aug 26 09:24:58 CEST 2023
spec:
  replicas: 1
  selector:
    matchLabels:
      app: nginx
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - image: nginx:alpine
        name: nginx
```

Let's use yupd to update the nginx image version as well as the last-updated annotation :

- For GitHub :

```bash
yupd --repo-type github --token <updateme> --project srozange/playground --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

- For GitLab :

```bash
yupd --repo-type gitlab --token <updateme> --project 48677990 --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

Voilà!

Additionally, instead of making direct updates to the target branch, yupd can create either a merge request or a pull request (based on the Git provider context) by simply adding the **--merge-request** or **--pull-request** flag.

## YAML path expressions

Yupd uses the [YamlPath](https://github.com/yaml-path/YamlPath) library.
You can check their [readme page](https://github.com/yaml-path/YamlPath) for the syntax.

## Manual
```bash
Usage: yupd [-hV] [--dry-run] [--pull-request] [--verbose] -b=<branch>
            [-f=<sourceFile>] [-m=<message>] -p=<path> --project=<project>
            [-r=<url>] --repo-type=<repoType> -t=<token> --set=<String=String>
            [--set=<String=String>]...
  -b, --branch=<branch>     Specifies the branch name of the target file to
                              update
      --dry-run             If set to true, no write operation is done
  -f, --template=<sourceFile>
                            Points to a local YAML file to be used as the
                              source, instead of the remote one
  -h, --help                Show this help message and exit.
  -m, --commit-msg=<commitMessage>
                            Provides a custom commit message for the update
  -p, --path=<path>         Specifies the path of the target file to update
      --project=<project>   Identifies the project (e.g., 'srozange/yupd' for
                              GitHub or '48539100' for GitLab)
      --pull-request, --merge-request
                            If set to true, open either a pull request or a
                              merge request based on the Git provider context
  -r, --repo=<url>          Specifies the URL of the Git gitRepository
      --repo-type=<repoType>
                            Specifies the gitRepository type; valid values:
                              'gitlab' or 'github'
      --set=<String=String> Allows setting YAML path expressions (e.g.,
                              metadata.name=new_name)
  -t, --token=<token>       Provides the authentication token
  -V, --version             Print version information and exit.
      --verbose             If set to true, sets the log level to debug
```

## Installation

You can grab the latest binaries from the [releases page](https://github.com/srozange/yupd/releases).

## Docker Image

Docker images are available on [Docker Hub](https://hub.docker.com/gitRepository/docker/srozange/yupd/general).

To use the image, you can run the following command:

```bash
docker run srozange/yupd:0.2 --repo-type github --token <token> ...
```

## Limitations

- Only supports modifications to one file per commit.