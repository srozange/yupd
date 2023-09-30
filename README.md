# Yupd
![GitHub release (with filter)](https://img.shields.io/github/v/release/srozange/yupd)
[![Build](https://github.com/srozange/yupd/actions/workflows/maven.yml/badge.svg)](https://github.com/srozange/yupd/actions/workflows/maven.yml)
[![codecov](https://codecov.io/gh/srozange/yupd/branch/main/badge.svg?token=JCPP4VZ1S1)](https://codecov.io/gh/srozange/yupd)

**Update YAML files the GitOps way**

Yupd is a command-line tool that allows updating a YAML file in a remote GitHub or GitLab repository.

## Usage

Assuming we have this file in a git repository:

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

```shell
yupd --repo-type github --token <updateme> --project srozange/playground --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

- For GitLab :

```shell
yupd --repo-type gitlab --token <updateme> --project 48677990 --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion --set metadata.annotations.last-updated="$(date)"
```

Voilà!

Additionally, instead of making direct updates to the target branch, yupd can create either a merge request or a pull request (based on the Git provider context) by simply adding the **--merge-request** or **--pull-request** flag.

## Updating files

You can specify how the file is updated using the repeatable `--set` option:

```shell
--set [type:]expression=value
```

Available types are : ```ypath``` and ```regex```.

We will use the following file for the subsequent examples:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: oldname
```

### YAML path expressions

Yupd uses the [YamlPath](https://github.com/yaml-path/YamlPath) library.

Here's an example where we change the name field to 'newname':

```shell
--set ypath:metadata.name=newname
```

Or you can use the shorthand:

```shell
--set metadata.name=newname
```

For more detailed syntax information, you can refer to the [YamlPath readme page](https://github.com/yaml-path/YamlPath).

### Regular Expressions

Yupd can also update files using regular expressions, which can be especially useful when you need to modify a file that isn't in YAML format.

Here's an example where we change the name field to 'newname':

```shell
--set "regex:name: (.*)=newname"
```

The text matched within the parentheses will be replaced with the right part.

For more information on the syntax, you can refer to the [Javadoc of class 'Pattern'](https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html).

## Manual
```shell
Usage: yupd [-hV] [--dry-run] [--insecure] [--pull-request] [--verbose] -b=<branch> [-f=<sourceFile>] [-m=<commitMessage>] -p=<path> --project=<project>
            [-r=<url>] --repo-type=<repoType> -t=<token> --set=<String=String>[||<String=String>...] [--set=<String=String>[||<String=String>...]]...
  -b, --branch=<branch>     Specifies the branch name of the target file to update (env: YUPD_BRANCH)
      --dry-run             If set to true, no write operation is done (env: YUPD_DRY_RUN)
  -f, --template=<sourceFile>
                            Points to a local YAML file to be used as the source, instead of the remote one (env: YUPD_TEMPLATE)
  -h, --help                Show this help message and exit.
      --insecure            If set to true, disable SSL certificate validation (applicable to GitLab only) (env: YUPD_INSECURE)
  -m, --commit-msg=<commitMessage>
                            Provides a custom commit message for the update (env: YUPD_COMMIT_MSG)
  -p, --path=<path>         Specifies the path of the target file to update (env: YUPD_PATH)
      --project=<project>   Identifies the project (e.g., 'srozange/yupd' for GitHub or '48539100' for GitLab) (env: YUPD_PROJECT)
      --pull-request, --merge-request
                            If set to true, open either a pull request or a merge request based on the Git provider context (env: YUPD_MERGE_REQUEST)
  -r, --repo=<url>          Specifies the URL of the Git repository (env: YUPD_REPO)
      --repo-type=<repoType>
                            Specifies the repository type; valid values: 'gitlab' or 'github' (env: YUPD_REPO_TYPE)
      --set=<String=String>[||<String=String>...]
                            Allows setting YAML path expressions (e.g., metadata.name=new_name) or regular expressions (env: YUPD_SET)
  -t, --token=<token>       Provides the authentication token (env: YUPD_TOKEN)
  -V, --version             Print version information and exit.
      --verbose             If set to true, sets the log level to debug (env: YUPD_VERBOSE)
```

## Installation

You can grab the latest binaries from the [releases page](https://github.com/srozange/yupd/releases).

## Docker Image

Docker images are available on [Docker Hub](https://hub.docker.com/repository/docker/srozange/yupd).

To use the image, you can run the following command:

```bash
docker run --rm srozange/yupd:0.3 --repo-type github --token <updateme> --project srozange/playground --path k8s/deployment.yml --branch yupd-it --set *.containers[0].image=nginx:newversion
```

## Limitations

- Only supports modifications to one file per commit.