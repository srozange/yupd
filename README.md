# Yupd
[![Build](https://github.com/srozange/yupd/actions/workflows/maven.yml/badge.svg)](https://github.com/srozange/yupd/actions/workflows/maven.yml)

**Update YAML files the GitOps way**

yupd is a command-line tool that allows updating a YAML file in a remote GitHub or GitLab repository.

## Usage

Assuming we have this file in a Git repository:

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

Let's use yupd to update the nginx image version as well as the last-updated annotation

For GitHub :

```bash
yupd --repo-type github --token <updateme> --project srozange/playground --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

For GitLab :

```bash
yupd --repo-type gitlab --token <updateme> --project 48677990 --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

Voilà!

## YAML path expressions

Yupd uses the [YamlPath](https://github.com/yaml-path/YamlPath) library.
You can check their [readme page](https://github.com/yaml-path/YamlPath) for the syntax.

## Manual
```bash
Usage: yupd [-hV] [--dry-run] [--verbose] -b=<branch> [-f=<sourceFile>]
            [-m=<commitMessage>] -p=<path> --project=<project> [-r=<url>]
            --repo-type=<repoType> -t=<token> --set=<String=String>
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
  -r, --repo=<url>          Specifies the URL of the Git repository
      --repo-type=<repoType>
                            Specifies the repository type; valid values:
                              'gitlab' or 'github'
      --set=<String=String> Allows setting YAML path expressions (e.g.,
                              metadata.name=new_name)
  -t, --token=<token>       Provides the authentication token
  -V, --version             Print version information and exit.
      --verbose             If set to true, sets the log level to debug
```

## Installation

You can grab the latest binaries from the [releases page](https://github.com/srozange/yupd/releases).

## Docker image

Coming soon.

## Limitations

- Does not currently support updating multiple documents embedded in a single file.
- Does not currently support the creation of merge requests or pull requests.
- Only supports modifications to one file per commit.