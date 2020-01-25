#!/bin/sh

echo -e '\ninstalled.tenants=junit' >> $BITBUCKET_CLONE_DIR/core-customize/hybris/config/local.properties

cd $BITBUCKET_CLONE_DIR/core-customize/hybris/bin/platform

. ./setantenv.sh

ant all

ant unittests -Dtestclasses.packages=uk.co.thebodyshop.* -Dtestclasses.suppress.junit.tenant=true

cp -r $BITBUCKET_CLONE_DIR/core-customize/hybris/log/junit $BITBUCKET_CLONE_DIR/test-results

test $(xmllint --xpath 'sum(/testsuites/testsuite/@failures) + sum(/testsuites/testsuite/@errors)' $BITBUCKET_CLONE_DIR/test-results/TESTS-TestSuites.xml) -eq 0
