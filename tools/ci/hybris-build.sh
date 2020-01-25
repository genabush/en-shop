#!/bin/sh

cd $BITBUCKET_CLONE_DIR/build

./gradlew -PENVOY_REPO_PASS=$ENVOY_REPO_PASS

cd $BITBUCKET_CLONE_DIR/core-customize/hybris/bin/platform

. ./setantenv.sh

ant clean
ant customize
ant all
