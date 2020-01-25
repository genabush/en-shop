/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
module.exports = function() {

    return {
        targets: [
            'smartEdit',
            'smartEditContainer',
            'e2e',
            'typescript'
        ],
        config: function(data, conf) {
            return {
                options: {
                    dest: 'jsTarget/docs',
                    title: "theBodyShopsmartedit API",
                    startPage: '/#/theBodyShopsmartedit',
                },
                smartEdit: {
                    api: true,
                    src: [
                        'web/features/theBodyShopsmartedit/**/*.+(js|ts)',
                        'web/features/theBodyShopsmarteditcommons/**/*.+(js|ts)'
                    ],
                    title: 'theBodyShopsmartedit'
                },
                smartEditContainer: {
                    api: true,
                    src: [
                        'web/features/theBodyShopsmartedit/**/*.+(js|ts)',
                        'web/features/theBodyShopsmarteditcommons/**/*.+(js|ts)'
                    ],
                    title: 'theBodyShopsmarteditContainer'
                },
                e2e: {
                    title: 'How-to: e2e Test Setup',
                    src: [
                        'smartedit-custom-build/docs/e2eSetupNgdocs.js'
                    ]
                },
                typescript: {
                    title: 'TypeScript',
                    src: [
                        'smartedit-custom-build/docs/typescript.ts'
                    ]
                }
            };
        }
    };

};
