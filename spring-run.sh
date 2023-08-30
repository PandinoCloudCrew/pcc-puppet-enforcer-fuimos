#!/bin/bash

export SPRING_PROFILES_ACTIVE=local
export SPRING_CLOUD_VAULT_AUTHENTICATION=token
export SPRING_CLOUD_VAULT_ENABLED=true
export SPRING_CLOUD_VAULT_HOST=vault-dev.ops.pandino.co
export SPRING_CLOUD_VAULT_KV_BACKEND=local
export SPRING_CLOUD_VAULT_PORT=443
export SPRING_CLOUD_VAULT_SCHEME=https
export SPRING_CLOUD_VAULT_TOKEN=hvs.QJswihsgYxhRc08qKyLama3l
export SPRING_CONFIG_IMPORT=vault://

VERSION=$(./gradlew properties -q | awk '/^version:/ {print $2}')

/c/Users/yesid/.sdkman/candidates/java/21.0.35-openjdk/bin/java -jar build/libs/pcc-puppet-enforcer-fuimos-$VERSION.jar