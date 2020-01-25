/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
const lodash = require('lodash');

const base = require('../../smartedit-build/config/karma/karma.ext.smartedit.conf');
const bundlePaths = require('../../smartedit-build/bundlePaths');

const {
    compose,
    merge,
    add
} = require('../../smartedit-build/builders');

const karma = compose(
    merge({
        junitReporter: {
            outputDir: 'jsTarget/tests/theBodyShopsmartedit/junit/', // results will be saved as $outputDir/$browserName.xml
            outputFile: 'testReport.xml' // if included, results will be saved as $outputDir/$browserName/$outputFile
        },

        // list of files / patterns to load in the browser
        files: lodash.concat(
            bundlePaths.test.unit.smarteditThirdPartyJsFiles,
            bundlePaths.test.unit.commonUtilModules, [
                'jsTarget/web/features/theBodyShopsmarteditcommons/**/*.js',
                'jsTarget/web/features/theBodyShopsmartedit/**/*.js',
                'jsTarget/web/features/theBodyShopsmartedit/templates.js',
                'jsTests/tests/theBodyShopsmartedit/unit/specBundle.ts'
            ]
        ),

        webpack: require('../webpack/webpack.karma.smartedit.config'),
    }),
    add('exclude', [
        'jsTarget/web/features/theBodyShopsmartedit/theBodyShopsmarteditModule.ts',
        '**/*.d.ts',
        '*.d.ts'
    ], true)
)(base);

module.exports = function(config) {
    config.set(karma);
};
