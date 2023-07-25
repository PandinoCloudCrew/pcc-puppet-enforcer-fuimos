# Code Health
| SonarCloud                                                                                                                                                                                            | CodeScene                                                                                                                            | Workflows                                                                                                                                                                                                                                  |
|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=pcc-puppet-enforcer-fuimos&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=pcc-puppet-enforcer-fuimos) | [![CodeScene general](https://codescene.io/images/analyzed-by-codescene-badge.svg)](https://codescene.io/projects/29004)             | [![CodeQL](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/codeql-analysis.yml)            |
| [![Coverage](https://sonarcloud.io/api/project_badges/measure?project=pcc-puppet-enforcer-fuimos&metric=coverage)](https://sonarcloud.io/summary/new_code?id=pcc-puppet-enforcer-fuimos)                | [![CodeScene Code Health](https://codescene.io/projects/29004/status-badges/code-health)](https://codescene.io/projects/29004)       | [![Sonar Code Coverage](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/check-coverage.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/check-coverage.yml) |
| [![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=pcc-puppet-enforcer-fuimos&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=pcc-puppet-enforcer-fuimos)       | [![CodeScene System Mastery](https://codescene.io/projects/29004/status-badges/system-mastery)](https://codescene.io/projects/29004) | [![Release](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/release.yml/badge.svg)](https://github.com/PandinoCloudCrew/pcc-puppet-enforcer-fuimos/actions/workflows/release.yml)                           |
| [![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=pcc-puppet-enforcer-fuimos&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=pcc-puppet-enforcer-fuimos)          | [![CodeScene Missed Goals](https://codescene.io/projects/29004/status-badges/missed-goals)](https://codescene.io/projects/29004)     |                                                                                                                                                                                                                                            |

## Micronaut 3.6.1 Documentation

- [User Guide](https://docs.micronaut.io/3.6.1/guide/index.html)
- [API Reference](https://docs.micronaut.io/3.6.1/api/index.html)
- [Configuration Reference](https://docs.micronaut.io/3.6.1/guide/configurationreference.html)
- [Micronaut Guides](https://guides.micronaut.io/index.html)
---

## Push To Docker Registry Workflow

Workflow file: [`.github/workflows/gradle.yml`](.github/workflows/gradle.yml)

### Workflow description
For pushes to the `master` branch, the workflow will:
1. Setup the build environment with respect to the selected java/graalvm version.
2. Login to docker registry based on provided configuration.
3. Build, tag and push Docker image 
4. with Micronaut application to the Docker container image.

### Dependencies on other GitHub Actions
- [Docker login](`https://github.com/docker/login-action`)(`docker/login`)
- [Setup GraalVM](`https://github.com/DeLaGuardo/setup-graalvm`)(`DeLaGuardo/setup-graalvm`)

### Setup
Add the following GitHub secrets:

| Name                   | Description                                                                                                          |
|------------------------|----------------------------------------------------------------------------------------------------------------------|
| DOCKER_USERNAME        | Username for Docker registry authentication.                                                                         |
| DOCKER_PASSWORD        | Docker registry password.                                                                                            |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL    | Docker registry url.                                                                                                 |

#### Configuration examples
Specifics on how to configure public cloud docker registries like DockerHub, Google Container Registry (GCR), AWS Container Registry (ECR),
Oracle Cloud Infrastructure Registry (OCIR) and many more can be found in [docker/login-action](https://github.com/docker/login-action)
documentation.

#### Google Container Registry (GCR)
Create service account with permission to edit GCR or use predefined Storage Admin role.

- `DOCKER_USERNAME` - set exactly to `_json_key`
- `DOCKER_PASSWORD` - content of the service account json key file
- `DOCKER_REPOSITORY_PATH` - `<project-id>/pcc-puppet-enforcer-fuimos`
- `DOCKER_REGISTRY_URL` - `gcr.io`

> See [docker/login-action for GCR](https://github.com/docker/login-action#google-container-registry-gcr)


## Push GraalVM Native Image To Docker Registry Workflow

Workflow file: [`.github/workflows/graalvm.yml`](.github/workflows/graalvm.yml)

### Workflow description
For pushes to the `master` branch, the workflow will:
1. Setup the build environment with respect to the selected java/graalvm version.
2. Login to docker registry based on provided configuration.
3. Build, tag and push Docker image with Micronaut application to the Docker container image.

### Dependencies on other GitHub Actions
- [Docker login](`https://github.com/docker/login-action`)(`docker/login`)
- [Setup GraalVM](`https://github.com/DeLaGuardo/setup-graalvm`)(`DeLaGuardo/setup-graalvm`)

### Setup
Add the following GitHub secrets:

| Name                   | Description                                                                                                          |
|------------------------|----------------------------------------------------------------------------------------------------------------------|
| DOCKER_USERNAME        | Username for Docker registry authentication.                                                                         |
| DOCKER_PASSWORD        | Docker registry password.                                                                                            |
| DOCKER_REPOSITORY_PATH | Path to the docker image repository inside the registry, e.g. for the image `foo/bar/micronaut:0.1` it is `foo/bar`. |
| DOCKER_REGISTRY_URL    | Docker registry url.                                                                                                 |

#### Configuration examples
Specifics on how to configure public cloud docker registries like DockerHub, Google Container Registry (GCR), AWS Container Registry (ECR),
Oracle Cloud Infrastructure Registry (OCIR) and many more can be found in [docker/login-action](https://github.com/docker/login-action)
documentation.

#### Google Container Registry (GCR)
Create service account with permission to edit GCR or use predefined Storage Admin role.

- `DOCKER_USERNAME` - set exactly to `_json_key`
- `DOCKER_PASSWORD` - content of the service account json key file
- `DOCKER_REPOSITORY_PATH` - `<project-id>/pcc-puppet-enforcer-fuimos`
- `DOCKER_REGISTRY_URL` - `gcr.io`

> See [docker/login-action for GCR](https://github.com/docker/login-action#google-container-registry-gcr)

- [Jib Gradle Plugin](https://plugins.gradle.org/plugin/com.google.cloud.tools.jib)
- [Shadow Gradle Plugin](https://plugins.gradle.org/plugin/com.github.johnrengelman.shadow)

# Vault Configuration

Requires a secret engine named `credentials`
Requires a secret engine named `kv`

# Keycloak Configuration

Requires that default-realm-roles is modified to add the realm roles `inpersonation` and `manage-users`

Requires an existing client that has admin permissions to create other clients, groups and users.

Organization's Client is used to create it's users.