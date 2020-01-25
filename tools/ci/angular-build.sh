#!/bin/sh

cd $BITBUCKET_CLONE_DIR/js-storefront/tbsStore

yarn install

yarn build

test -f "$BITBUCKET_CLONE_DIR/js-storefront/tbsStore/dist/tbsStore/index.html"
